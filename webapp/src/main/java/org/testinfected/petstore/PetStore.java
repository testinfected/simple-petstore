package org.testinfected.petstore;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.petstore.endpoints.Home;
import org.testinfected.petstore.endpoints.Logout;
import org.testinfected.petstore.endpoints.ShowProducts;
import org.testinfected.petstore.decoration.HtmlDocumentProcessor;
import org.testinfected.petstore.decoration.HtmlPageSelector;
import org.testinfected.petstore.decoration.LayoutTemplate;
import org.testinfected.petstore.decoration.PageCompositor;
import org.testinfected.petstore.dispatch.Router;
import org.testinfected.petstore.dispatch.Routes;
import org.testinfected.petstore.jdbc.DataSourceProperties;
import org.testinfected.petstore.jdbc.DriverManagerDataSource;
import org.testinfected.petstore.jdbc.ProductsDatabase;
import org.testinfected.petstore.pipeline.ApacheCommonLogger;
import org.testinfected.petstore.pipeline.ConnectionManager;
import org.testinfected.petstore.pipeline.Dispatcher;
import org.testinfected.petstore.pipeline.Failsafe;
import org.testinfected.petstore.pipeline.FileServer;
import org.testinfected.petstore.pipeline.HttpMethodOverride;
import org.testinfected.petstore.pipeline.MiddlewareStack;
import org.testinfected.petstore.pipeline.ServerHeaders;
import org.testinfected.petstore.pipeline.SiteMesh;
import org.testinfected.petstore.pipeline.StaticAssets;
import org.testinfected.petstore.util.ConsoleErrorReporter;
import org.testinfected.petstore.util.ConsoleHandler;
import org.testinfected.petstore.util.FileSystemPhotoStore;
import org.testinfected.petstore.util.PlainFormatter;
import org.testinfected.time.lib.SystemClock;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

import static org.testinfected.petstore.pipeline.ConnectionManager.ConnectionReference;

public class PetStore {

    private static final String LOGGER_NAME = "access";

    private final WebLayout web;
    private final DataSource dataSource;
    private final Logger logger = makeLogger();
    private final SystemClock clock = new SystemClock();

    private Server server;
    private Charset outputEncoding = Charset.defaultCharset();
    private FailureReporter failureReporter = ConsoleErrorReporter.toStandardError();

    public PetStore(WebLayout layout, DataSource dataSource) {
        this.web = layout;
        this.dataSource = dataSource;
    }

    public void encodeOutputAs(String charsetName) {
        encodeOutputAs(Charset.forName(charsetName));
    }

    public void encodeOutputAs(Charset encoding) {
        this.outputEncoding = encoding;
    }

    public void reportErrorsTo(FailureReporter failureReporter) {
        this.failureReporter = failureReporter;
    }

    public void quiet() {
        reportErrorsTo(FailureReporter.IGNORE);
    }

    public void logToConsole() {
        logger.addHandler(ConsoleHandler.toStandardOutput());
    }

    public void logToFile(String logFile) throws IOException {
        logger.addHandler(fileHandler(logFile));
    }

    public void start(int port) throws Exception {
        server = new Server(port, failureReporter);
        server.run(new MiddlewareStack() {{
            use(new Failsafe(new MustacheRendering(new FileSystemResourceLoader(web.templates, web.encoding)), failureReporter));
            use(new ServerHeaders(clock));
            use(new HttpMethodOverride());
            use(new ApacheCommonLogger(logger, clock));
            use(staticAssets());
            use(siteMesh());
            use(new ConnectionManager(dataSource));
            run(new Application() {
                public void handle(Request request, Response response) throws Exception {
                    ConnectionReference connection = new ConnectionReference(request);
                    dispatcher(connection.get()).handle(request, response);
                }
            });
        }});
    }

    private Dispatcher dispatcher(Connection connection) {
        Renderer renderer = new MustacheRendering(new FileSystemResourceLoader(web.pages, web.encoding));
        final Dispatcher dispatcher = new Dispatcher(drawRoutes(connection), renderer);
        dispatcher.setEncoding(outputEncoding);
        return dispatcher;
    }

    private Router drawRoutes(final Connection connection) {
        Router router = new Router();
        router.draw(new Routes() {{
            map("/products").to(new ShowProducts(new ProductsDatabase(connection), new FileSystemPhotoStore("/photos")));
            delete("/logout").to(new Logout());
            otherwise().to(new Home());
        }});
        return router;
    }

    private SiteMesh siteMesh() {
        Renderer renderer = new MustacheRendering(new FileSystemResourceLoader(web.layouts, web.encoding));
        SiteMesh siteMesh = new SiteMesh(new HtmlPageSelector());
        siteMesh.map("/", new PageCompositor(new HtmlDocumentProcessor(), new LayoutTemplate("main", renderer)));
        return siteMesh;
    }

    public void stop() throws Exception {
        if (server != null) server.shutdown();
    }

    private static Logger makeLogger() {
        Logger logger = Logger.getLogger(LOGGER_NAME);
        logger.setUseParentHandlers(false);
        return logger;
    }

    private StaticAssets staticAssets() {
        final StaticAssets assets = new StaticAssets(new FileServer(new FileSystemResourceLoader(web.assets)));
        assets.serve("/favicon.ico", "/images", "/stylesheets", "/photos");
        return assets;
    }

    private Handler fileHandler(String logFile) throws IOException {
        FileHandler handler = new FileHandler(logFile);
        handler.setFormatter(new PlainFormatter());
        return handler;
    }
}
