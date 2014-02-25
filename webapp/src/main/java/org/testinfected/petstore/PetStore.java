package org.testinfected.petstore;

import org.testinfected.molecule.MiddlewareStack;
import org.testinfected.molecule.Server;
import org.testinfected.molecule.middlewares.ApacheCommonLogger;
import org.testinfected.molecule.middlewares.ConnectionScope;
import org.testinfected.molecule.middlewares.DateHeader;
import org.testinfected.molecule.middlewares.Failsafe;
import org.testinfected.molecule.middlewares.FailureMonitor;
import org.testinfected.molecule.middlewares.FileServer;
import org.testinfected.molecule.middlewares.HttpMethodOverride;
import org.testinfected.molecule.middlewares.ServerHeader;
import org.testinfected.molecule.middlewares.StaticAssets;
import org.testinfected.molecule.util.Clock;
import org.testinfected.molecule.util.FailureReporter;
import org.testinfected.molecule.util.SystemClock;
import org.testinfected.petstore.util.JMustacheRendering;
import org.testinfected.petstore.util.PlainFormatter;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class PetStore {

    public static final String NAME = "PetStore/0.2";
    public static final String PAGES_DIR = "app/pages";
    public static final String LAYOUT_DIR = "app/layout";
    public static final String PUBLIC_DIR = "public";

    private final File context;
    private final DataSource dataSource;
    private final Logger logger = makeLogger();

    private FailureReporter failureReporter = FailureReporter.IGNORE;
    private Clock clock = new SystemClock();

    public PetStore(File context, DataSource dataSource) {
        this.context = context;
        this.dataSource = dataSource;
    }

    public void reportErrorsTo(FailureReporter failureReporter) {
        this.failureReporter = failureReporter;
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }

    public void logTo(Handler handler) {
        handler.setFormatter(new PlainFormatter());
        logger.addHandler(handler);
    }

    public void start(Server server) throws IOException {
        server.run(new MiddlewareStack() {{
            use(new ServerHeader(NAME));
            use(new DateHeader(clock));
            use(new ApacheCommonLogger(logger, clock));
            use(new Failsafe());
            use(new FailureMonitor(failureReporter));
            use(new HttpMethodOverride());
            use(staticAssets());
            use(new SiteLayout(templatesIn(LAYOUT_DIR)));
            use(new ConnectionScope(dataSource));
            run(new Routing(Pages.using(templatesIn(PAGES_DIR))));
        }});
    }

    private static Logger makeLogger() {
        Logger logger = Logger.getAnonymousLogger();
        logger.setUseParentHandlers(false);
        return logger;
    }

    private StaticAssets staticAssets() {
        final StaticAssets assets = new StaticAssets(new FileServer(new File(context, PUBLIC_DIR)));
        assets.serve("/favicon.ico", "/images", "/stylesheets", "/photos");
        return assets;
    }

    private RenderingEngine templatesIn(final String dir) {
        return new JMustacheRendering(new File(context, dir));
    }
}
