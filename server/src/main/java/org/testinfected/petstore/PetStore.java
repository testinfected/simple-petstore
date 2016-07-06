package org.testinfected.petstore;

import com.vtence.molecule.FailureReporter;
import com.vtence.molecule.Middleware;
import com.vtence.molecule.WebServer;
import com.vtence.molecule.middlewares.ApacheCommonLogger;
import com.vtence.molecule.middlewares.ConnectionScope;
import com.vtence.molecule.middlewares.ContentLengthHeader;
import com.vtence.molecule.middlewares.CookieSessionTracker;
import com.vtence.molecule.middlewares.Cookies;
import com.vtence.molecule.middlewares.DateHeader;
import com.vtence.molecule.middlewares.Failsafe;
import com.vtence.molecule.middlewares.FailureMonitor;
import com.vtence.molecule.middlewares.FileServer;
import com.vtence.molecule.middlewares.HttpMethodOverride;
import com.vtence.molecule.middlewares.ServerHeader;
import com.vtence.molecule.middlewares.StaticAssets;
import com.vtence.molecule.session.CookieSessionStore;
import com.vtence.tape.DriverManagerDataSource;
import org.testinfected.petstore.lib.Logging;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Logger;

public class PetStore {

    public static final String NAME = "PetStore/0.2";

    private final WebServer server;

    private FailureReporter failureReporter = FailureReporter.IGNORE;
    private Logger logger = Logging.off();
    private int timeout = -1;

    public PetStore(String host, int port) {
        server = WebServer.create(host, port);
    }

    public void reportErrorsTo(FailureReporter failureReporter) {
        this.failureReporter = failureReporter;
    }

    public void logTo(Logger logger) {
        this.logger = logger;
    }

    public void sessionTimeout(int seconds) {
        this.timeout = seconds;
    }

    public URI uri() {
        return server.uri();
    }

    public void start(File webRoot, Settings settings) throws IOException {
        DataSource dataSource = new DriverManagerDataSource(settings.databaseUrl, settings.databaseUsername, settings.databasePassword);
        server.failureReporter(failureReporter)
              .add(new ApacheCommonLogger(logger))
              .add(new ServerHeader(NAME))
              .add(new DateHeader())
              .add(new ContentLengthHeader())
              .add(new Failsafe())
              .add(new FailureMonitor(failureReporter))
              .add(new HttpMethodOverride())
              .add(staticAssets(webRoot))
              .add(new Cookies())
              .add(sessionCookies())
              .add(new ConnectionScope(dataSource))
              .start(new WebApp(webRoot));
    }

    private Middleware sessionCookies() {
        CookieSessionTracker tracker = new CookieSessionTracker(CookieSessionStore.secure("secret"));
        tracker.expireAfter(timeout);
        tracker.reportFailureTo(failureReporter);
        return tracker;
    }

    public void stop() throws IOException {
        server.stop();
    }


    private StaticAssets staticAssets(File webRoot) {
        final StaticAssets assets = new StaticAssets(new FileServer(publicDir(webRoot)));
        assets.serve("/favicon.ico", "/images", "/stylesheets", "/photos");
        return assets;
    }

    private File publicDir(File webRoot) {
        return new File(webRoot, "public");
    }
}
