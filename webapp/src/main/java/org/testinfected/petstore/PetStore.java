package org.testinfected.petstore;

import com.vtence.molecule.FailureReporter;
import com.vtence.molecule.MiddlewareStack;
import com.vtence.molecule.Server;
import com.vtence.molecule.middlewares.ApacheCommonLogger;
import com.vtence.molecule.middlewares.ConnectionScope;
import com.vtence.molecule.middlewares.ContentLengthHeader;
import com.vtence.molecule.middlewares.Cookies;
import com.vtence.molecule.middlewares.CookieSessionTracker;
import com.vtence.molecule.middlewares.DateHeader;
import com.vtence.molecule.middlewares.Failsafe;
import com.vtence.molecule.middlewares.FailureMonitor;
import com.vtence.molecule.middlewares.FileServer;
import com.vtence.molecule.middlewares.FilterMap;
import com.vtence.molecule.middlewares.HttpMethodOverride;
import com.vtence.molecule.middlewares.Layout;
import com.vtence.molecule.middlewares.ServerHeader;
import com.vtence.molecule.middlewares.StaticAssets;
import com.vtence.molecule.session.PeriodicSessionHouseKeeping;
import com.vtence.molecule.session.SecureIdentifierPolicy;
import com.vtence.molecule.session.SessionPool;
import com.vtence.molecule.templating.JMustacheRenderer;
import com.vtence.molecule.templating.Template;
import com.vtence.molecule.templating.Templates;
import org.testinfected.petstore.util.Logging;
import org.testinfected.petstore.views.PlainPage;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

public class PetStore {

    public static final String NAME = "PetStore/0.2";
    public static final String PAGES_DIR = "views/pages";
    public static final String LAYOUT_DIR = "views/layout";
    public static final String PUBLIC_DIR = "public";

    private final File context;
    private final DataSource dataSource;
    private final ScheduledExecutorService scheduler = newSingleThreadScheduledExecutor();

    private FailureReporter failureReporter = FailureReporter.IGNORE;
    private Logger logger = Logging.off();
    private int timeout = -1;

    public PetStore(File context, DataSource dataSource) {
        this.context = context;
        this.dataSource = dataSource;
    }

    public void reportErrorsTo(FailureReporter failureReporter) {
        this.failureReporter = failureReporter;
    }

    public void logging(Logger logger) {
        this.logger = logger;
    }

    public void sessionTimeout(int seconds) {
        this.timeout = seconds;
    }

    public void start(Server server) throws IOException {
        server.run(new MiddlewareStack() {{
            use(new ApacheCommonLogger(logger));
            use(new ServerHeader(NAME));
            use(new DateHeader());
            use(new ContentLengthHeader());
            use(new Failsafe());
            use(new FailureMonitor(failureReporter));
            use(new HttpMethodOverride());
            use(staticAssets());
            use(new Cookies());
            use(new CookieSessionTracker(createSessionPool(timeout)).expireAfter(timeout));
            use(new FilterMap().map("/", Layout.html(new SiteLayout(layoutTemplate()))));
            use(new ConnectionScope(dataSource));
            run(new Routing(new Pages(pageTemplates())));
        }});
    }

    public void stop() {
        scheduler.shutdownNow();
    }

    private SessionPool createSessionPool(int timeout) {
        SessionPool sessions = new SessionPool(new SecureIdentifierPolicy());
        if (this.timeout > 0) {
            PeriodicSessionHouseKeeping houseKeeping =
                    new PeriodicSessionHouseKeeping(scheduler, sessions, timeout, TimeUnit.SECONDS);
            houseKeeping.start();
        }
        return sessions;
    }

    private StaticAssets staticAssets() {
        final StaticAssets assets = new StaticAssets(new FileServer(new File(context, PUBLIC_DIR)));
        assets.serve("/favicon.ico", "/images", "/stylesheets", "/photos");
        return assets;
    }

    private Template<PlainPage> layoutTemplate() {
        return templatesIn(LAYOUT_DIR).named("main");
    }

    private Templates pageTemplates() {
        return templatesIn(PAGES_DIR);
    }

    private Templates templatesIn(final String dir) {
        return new Templates(new JMustacheRenderer().fromDir(new File(context, dir))
                                                    .extension("html")
                                                    .encoding("utf-8")
                                                    .defaultValue(""));
    }
}
