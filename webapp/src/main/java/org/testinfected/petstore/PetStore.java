package org.testinfected.petstore;

import org.testinfected.petstore.decoration.HtmlDocumentProcessor;
import org.testinfected.petstore.decoration.HtmlPageSelector;
import org.testinfected.petstore.decoration.LayoutTemplate;
import org.testinfected.petstore.decoration.PageCompositor;
import org.testinfected.petstore.pipeline.ApacheCommonLogger;
import org.testinfected.petstore.pipeline.ConnectionManager;
import org.testinfected.petstore.pipeline.Failsafe;
import org.testinfected.petstore.pipeline.FileServer;
import org.testinfected.petstore.pipeline.HttpMethodOverride;
import org.testinfected.petstore.pipeline.MiddlewareStack;
import org.testinfected.petstore.pipeline.ServerHeaders;
import org.testinfected.petstore.pipeline.SiteMesh;
import org.testinfected.petstore.pipeline.StaticAssets;
import org.testinfected.petstore.util.ConsoleErrorReporter;
import org.testinfected.petstore.util.PlainFormatter;
import org.testinfected.time.lib.SystemClock;

import javax.sql.DataSource;
import java.io.File;
import java.nio.charset.Charset;
import java.util.logging.Handler;
import java.util.logging.Logger;

import static org.testinfected.petstore.util.Charsets.UTF_8;

public class PetStore {

    public static final String APP_DIR = "app";
    public static final String PAGES_DIR = "app/pages";
    public static final String LAYOUT_DIR = "app/layout";
    public static final String PUBLIC_DIR = "public";

    private static final String LOGGER_NAME = "access";

    private final File context;
    private final DataSource dataSource;
    private final Logger logger = makeLogger();

    private final SystemClock clock = new SystemClock();
    private Server server;
    private Charset outputEncoding = Charset.defaultCharset();
    private FailureReporter failureReporter = ConsoleErrorReporter.toStandardError();

    public PetStore(File context, DataSource dataSource) {
        this.context = context;
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

    public void logTo(Handler handler) {
        handler.setFormatter(new PlainFormatter());
        logger.addHandler(handler);
    }

    // todo Consider either passing in the server instead of the port or assembling the application
    public void start(int port) throws Exception {
        server = new Server(port, failureReporter);
        server.run(new MiddlewareStack() {{
            use(new Failsafe(new MustacheRendering(new FileSystemResourceLoader(new File(context, APP_DIR), UTF_8)), failureReporter));
            use(new ServerHeaders(clock));
            use(new HttpMethodOverride());
            use(new ApacheCommonLogger(logger, clock));
            use(staticAssets());
            use(siteMesh());
            use(new ConnectionManager(dataSource));
            run(new Routing(new MustacheRendering(new FileSystemResourceLoader(new File(context, PAGES_DIR), UTF_8)), outputEncoding));
        }});
    }

    private SiteMesh siteMesh() {
        Renderer renderer = new MustacheRendering(new FileSystemResourceLoader(new File(context, LAYOUT_DIR), UTF_8));
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
        final StaticAssets assets = new StaticAssets(new FileServer(new FileSystemResourceLoader(new File(context, PUBLIC_DIR), UTF_8)));
        assets.serve("/favicon.ico", "/images", "/stylesheets", "/photos");
        return assets;
    }
}
