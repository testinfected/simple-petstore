package org.testinfected.petstore;

import com.vtence.molecule.MiddlewareStack;
import com.vtence.molecule.Server;
import com.vtence.molecule.middlewares.ApacheCommonLogger;
import com.vtence.molecule.middlewares.ConnectionScope;
import com.vtence.molecule.middlewares.DateHeader;
import com.vtence.molecule.middlewares.Failsafe;
import com.vtence.molecule.middlewares.FailureMonitor;
import com.vtence.molecule.middlewares.FileServer;
import com.vtence.molecule.middlewares.HttpMethodOverride;
import com.vtence.molecule.middlewares.ServerHeader;
import com.vtence.molecule.middlewares.StaticAssets;
import com.vtence.molecule.util.Clock;
import com.vtence.molecule.util.FailureReporter;
import com.vtence.molecule.util.SystemClock;
import org.testinfected.petstore.util.JMustacheRendering;
import org.testinfected.petstore.util.Logging;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class PetStore {

    public static final String NAME = "PetStore/0.2";
    public static final String PAGES_DIR = "app/pages";
    public static final String LAYOUT_DIR = "app/layout";
    public static final String PUBLIC_DIR = "public";

    private final File context;
    private final DataSource dataSource;

    private FailureReporter failureReporter = FailureReporter.IGNORE;
    private Logger logger = Logging.off();
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

    public void logging(Logger logger) {
        this.logger = logger;
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

    private StaticAssets staticAssets() {
        final StaticAssets assets = new StaticAssets(new FileServer(new File(context, PUBLIC_DIR)));
        assets.serve("/favicon.ico", "/images", "/stylesheets", "/photos");
        return assets;
    }

    private RenderingEngine templatesIn(final String dir) {
        return new JMustacheRendering(new File(context, dir));
    }
}
