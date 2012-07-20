package org.testinfected.petstore;

import org.testinfected.petstore.destinations.Home;
import org.testinfected.petstore.destinations.Logout;
import org.testinfected.petstore.destinations.ShowProducts;
import org.testinfected.petstore.decoration.HtmlDocumentProcessor;
import org.testinfected.petstore.decoration.HtmlPageSelector;
import org.testinfected.petstore.decoration.LayoutTemplate;
import org.testinfected.petstore.decoration.PageCompositor;
import org.testinfected.petstore.dispatch.Router;
import org.testinfected.petstore.dispatch.Routes;
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
import org.testinfected.petstore.util.PlainFormatter;
import org.testinfected.time.lib.SystemClock;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class PetStore {

    public static final String TEMPLATE_DIRECTORY = "templates";
    public static final String ASSET_DIRECTORY = "assets";
    private static final String LOGGER_NAME = "access";

    private final File location;
    private final Logger logger = makeLogger();

    private Server server;

    private Charset charset = Charset.defaultCharset();
    private FailureReporter failureReporter = ConsoleErrorReporter.toStandardError();
    final SystemClock clock = new SystemClock();

    public static PetStore at(String webRoot) {
        return new PetStore(new File(webRoot));
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

    public void start(int port) throws IOException {
        final Renderer renderer = new MustacheRendering(new FileSystemResourceLoader(templateDirectory(), Charsets.UTF_8));

        server = new Server(port, failureReporter);
        server.run(new MiddlewareStack() {{
            use(new Failsafe(renderer, failureReporter));
            use(new ServerHeaders(clock));
            use(new HttpMethodOverride());
            use(new ApacheCommonLogger(logger, clock));
            use(staticAssets());
            use(siteMesh(renderer));
            run(dispatcher(renderer));
        }});
    }

    private Dispatcher dispatcher(Renderer renderer) {
        final Dispatcher dispatcher = new Dispatcher(new Router(new Home()), renderer);
        dispatcher.setEncoding(charset);
        dispatcher.draw(new Routes() {{
            match("/products").to(new ShowProducts());
            delete("/logout").to(new Logout());
        }});
        return dispatcher;
    }

    private SiteMesh siteMesh(Renderer renderer) {
        SiteMesh siteMesh = new SiteMesh(new HtmlPageSelector());
        siteMesh.map("/", new PageCompositor(new HtmlDocumentProcessor(), new LayoutTemplate("layout/main", renderer)));
        return siteMesh;
    }

    public void stop() throws IOException {
        if (server != null) server.shutdown();
    }

    private static Logger makeLogger() {
        Logger logger = Logger.getLogger(LOGGER_NAME);
        logger.setUseParentHandlers(false);
        return logger;
    }

    private StaticAssets staticAssets() {
        final StaticAssets assets = new StaticAssets(new FileServer(new FileSystemResourceLoader(assetDirectory())));
        assets.serve("/favicon.ico", "/images", "/stylesheets");
        return assets;
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
