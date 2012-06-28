package org.testinfected.petstore;

import org.testinfected.petstore.pipeline.ApacheCommonLogger;
import org.testinfected.petstore.pipeline.Dispatcher;
import org.testinfected.petstore.pipeline.Failsafe;
import org.testinfected.petstore.pipeline.FileServer;
import org.testinfected.petstore.pipeline.MiddlewareStack;
import org.testinfected.petstore.pipeline.ServerHeaders;
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

    private final File root;
    private final Logger logger = makeLogger();

    private Server server;

    private Charset charset = Charset.defaultCharset();
    private FailureReporter failureReporter = ConsoleErrorReporter.toStandardError();
    final SystemClock clock = new SystemClock();;

    public static PetStore rootedAt(File root) {
        return new PetStore(root);
    }

    public PetStore(File root) {
        this.root = root;
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

    public void start(int port) throws IOException {
        server = new Server(port, failureReporter);
        final Renderer renderer = new MustacheRendering(new FileSystemResourceLoader(templateDirectory(), Charsets.UTF_8));
        server.run(new MiddlewareStack() {{
            use(new Failsafe(renderer, failureReporter));
            use(new ServerHeaders(clock));
            use(new ApacheCommonLogger(logger, clock));
            use(staticAssets());
            run(new Dispatcher(renderer, charset));
        }});
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
        return new File(root, TEMPLATE_DIRECTORY);
    }

    private File assetDirectory() {
        return new File(root, ASSET_DIRECTORY);
    }

    public void logToFile(String logFile) throws IOException {
        logger.addHandler(fileHandler(logFile));
    }

    private Handler fileHandler(String logFile) throws IOException {
        FileHandler handler = new FileHandler(logFile);
        handler.setFormatter(new PlainFormatter());
        return handler;
    }

    public void logToConsole() {
        logger.addHandler(ConsoleHandler.toStandardOutput());
    }
}
