package org.testinfected.petstore;

import org.testinfected.petstore.util.MustacheRendering;
import org.testinfected.support.FailureReporter;
import org.testinfected.support.MiddlewareStack;
import org.testinfected.support.Server;
import org.testinfected.support.middlewares.ApacheCommonLogger;
import org.testinfected.support.middlewares.ConnectionScope;
import org.testinfected.support.middlewares.Failsafe;
import org.testinfected.support.middlewares.FileServer;
import org.testinfected.support.middlewares.HttpMethodOverride;
import org.testinfected.support.middlewares.ServerHeaders;
import org.testinfected.support.middlewares.StaticAssets;
import org.testinfected.support.util.PlainFormatter;
import org.testinfected.time.lib.SystemClock;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class PetStore {

    public static final String PAGES_DIR = "app/pages";
    public static final String LAYOUT_DIR = "app/layout";
    public static final String PUBLIC_DIR = "public";

    private static final String LOGGER_NAME = "access";

    private final File context;
    private final DataSource dataSource;
    private final Logger logger = makeLogger();
    private final SystemClock clock = new SystemClock();

    private FailureReporter failureReporter = FailureReporter.IGNORE;

    public PetStore(File context, DataSource dataSource) {
        this.context = context;
        this.dataSource = dataSource;
    }

    public void reportErrorsTo(FailureReporter failureReporter) {
        this.failureReporter = failureReporter;
    }

    public void logTo(Handler handler) {
        handler.setFormatter(new PlainFormatter());
        logger.addHandler(handler);
    }

    public void start(Server server) throws IOException {
        server.run(new MiddlewareStack() {{
            use(new ApacheCommonLogger(logger, clock));
            use(new Failsafe(failureReporter));
            use(new ServerHeaders(clock));
            use(new HttpMethodOverride());
            use(staticAssets());
            use(new SiteLayout(rendererFrom(LAYOUT_DIR)));
            use(new ConnectionScope(dataSource));
            run(new Routing(Pages.using(rendererFrom(PAGES_DIR))));
        }});
    }

    private static Logger makeLogger() {
        Logger logger = Logger.getLogger(LOGGER_NAME);
        logger.setUseParentHandlers(false);
        return logger;
    }

    private StaticAssets staticAssets() {
        final StaticAssets assets = new StaticAssets(new FileServer(new File(context, PUBLIC_DIR)));
        assets.serve("/favicon.ico", "/images", "/stylesheets", "/photos");
        return assets;
    }

    private MustacheRendering rendererFrom(final String dir) {
        return new MustacheRendering(new File(context, dir));
    }
}
