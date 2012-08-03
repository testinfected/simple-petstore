package org.testinfected.petstore;

import org.testinfected.petstore.endpoints.Home;
import org.testinfected.petstore.endpoints.Logout;
import org.testinfected.petstore.endpoints.ShowProducts;
import org.testinfected.petstore.decoration.HtmlDocumentProcessor;
import org.testinfected.petstore.decoration.HtmlPageSelector;
import org.testinfected.petstore.decoration.LayoutTemplate;
import org.testinfected.petstore.decoration.PageCompositor;
import org.testinfected.petstore.dispatch.Router;
import org.testinfected.petstore.dispatch.Routes;
import org.testinfected.petstore.jdbc.DriverManagerDataSource;
import org.testinfected.petstore.jdbc.ProductsDatabase;
import org.testinfected.petstore.pipeline.ApacheCommonLogger;
import org.testinfected.petstore.pipeline.Dispatcher;
import org.testinfected.petstore.pipeline.Failsafe;
import org.testinfected.petstore.pipeline.FileServer;
import org.testinfected.petstore.pipeline.HttpMethodOverride;
import org.testinfected.petstore.pipeline.MiddlewareStack;
import org.testinfected.petstore.pipeline.ServerHeaders;
import org.testinfected.petstore.pipeline.SiteMesh;
import org.testinfected.petstore.pipeline.StaticAssets;
import org.testinfected.petstore.util.Charsets;
import org.testinfected.petstore.util.ConsoleErrorReporter;
import org.testinfected.petstore.util.ConsoleHandler;
import org.testinfected.petstore.util.FileSystemPhotoStore;
import org.testinfected.petstore.util.PlainFormatter;
import org.testinfected.time.lib.SystemClock;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class PetStore {

    public static final String TEMPLATE_DIRECTORY = "templates";
    public static final String ASSET_DIRECTORY = "assets";
    public static final String PAGES_DIRECTORY = "pages";
    public static final String LAYOUT_DIRECTORY = "layout";

    private static final String LOGGER_NAME = "access";

    private final File location;
    private final Logger logger = makeLogger();
    private final SystemClock clock = new SystemClock();

    private Server server;
    private Connection connection;
    private Charset charset = Charset.defaultCharset();
    private FailureReporter failureReporter = ConsoleErrorReporter.toStandardError();

    public static PetStore at(String webRoot) {
        return at(new File(webRoot));
    }

    public static PetStore at(File webRoot) {
        return new PetStore(webRoot);
    }

    public PetStore(File webRoot) {
        this.location = webRoot;
    }

    public void encodeOutputAs(String charsetName) {
        encodeOutputAs(Charset.forName(charsetName));
    }

    public void encodeOutputAs(Charset charset) {
        this.charset = charset;
    }

    public void quiet() {
        failureReporter = FailureReporter.IGNORE;
    }

    public void logToConsole() {
        logger.addHandler(ConsoleHandler.toStandardOutput());
    }

    public void logToFile(String logFile) throws IOException {
        logger.addHandler(fileHandler(logFile));
    }

    public void start(int port) throws Exception {
        // mouahahaha wtf a single connection !?!
        // todo scope this by http request
        connection = connectToDatabase();
        server = new Server(port, failureReporter);
        server.run(new MiddlewareStack() {{
            use(new Failsafe(new MustacheRendering(new FileSystemResourceLoader(templateDirectory(), Charsets.UTF_8)), failureReporter));
            use(new ServerHeaders(clock));
            use(new HttpMethodOverride());
            use(new ApacheCommonLogger(logger, clock));
            use(staticAssets());
            use(siteMesh());
            run(dispatcher());
        }});
    }

    private Dispatcher dispatcher() {
        Renderer renderer = new MustacheRendering(new FileSystemResourceLoader(pagesDirectory(), Charsets.UTF_8));
        final Dispatcher dispatcher = new Dispatcher(drawRoutes(), renderer);
        dispatcher.setEncoding(charset);
        return dispatcher;
    }

    private Router drawRoutes() {
        Router router = new Router();
        router.draw(new Routes() {{
            map("/products").to(new ShowProducts(new ProductsDatabase(connection), new FileSystemPhotoStore("/photos")));
            delete("/logout").to(new Logout());
            otherwise().to(new Home());
        }});
        return router;
    }

    private Connection connectToDatabase() throws SQLException {
        String jdbcUrl= "jdbc:mysql://localhost:3306/petstore_test";
        String jdbcUsername = "testbot";
        String jdbcPassword = "petstore";
        DataSource dataSource = new DriverManagerDataSource(jdbcUrl, jdbcUsername, jdbcPassword);
        return dataSource.getConnection();
    }

    private SiteMesh siteMesh() {
        Renderer renderer = new MustacheRendering(new FileSystemResourceLoader(layoutDirectory(), Charsets.UTF_8));
        SiteMesh siteMesh = new SiteMesh(new HtmlPageSelector());
        siteMesh.map("/", new PageCompositor(new HtmlDocumentProcessor(), new LayoutTemplate("main", renderer)));
        return siteMesh;
    }

    public void stop() throws Exception {
        if (server != null) server.shutdown();
        if (connection != null) connection.close();
    }

    private static Logger makeLogger() {
        Logger logger = Logger.getLogger(LOGGER_NAME);
        logger.setUseParentHandlers(false);
        return logger;
    }

    private StaticAssets staticAssets() {
        final StaticAssets assets = new StaticAssets(new FileServer(new FileSystemResourceLoader(assetDirectory())));
        assets.serve("/favicon.ico", "/images", "/stylesheets", "/photos");
        return assets;
    }

    private File pagesDirectory() {
        return new File(templateDirectory(), PAGES_DIRECTORY);
    }

    private File layoutDirectory() {
        return new File(templateDirectory(), LAYOUT_DIRECTORY);
    }

    private File templateDirectory() {
        return new File(location, TEMPLATE_DIRECTORY);
    }

    private File assetDirectory() {
        return new File(location, ASSET_DIRECTORY);
    }

    private Handler fileHandler(String logFile) throws IOException {
        FileHandler handler = new FileHandler(logFile);
        handler.setFormatter(new PlainFormatter());
        return handler;
    }


}
