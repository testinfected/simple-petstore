package test.support.com.pyxis.petstore.web.serverdriver;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.DefaultHandler;
import org.mortbay.jetty.handler.HandlerList;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.thread.QueuedThreadPool;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import static com.pyxis.matchers.ExceptionImposter.imposterize;
import static test.support.com.pyxis.petstore.web.Environment.contextPath;
import static test.support.com.pyxis.petstore.web.Environment.serverHost;
import static test.support.com.pyxis.petstore.web.Environment.serverPort;

public class JettyDriver implements ServerDriver {

    private static final String WEBAPP_PROPERTIES_FILE = "/webapp.properties";
    private static final int SHUTDOWN_TIMEOUT = 1000;
    private static final int MAX_THREADS = 5;

    private final Server jetty = new Server();

    private static String webappPath() {
        try {
            Properties webappProperties = webappProperties();
            return webappProperties.getProperty("webapp.path");
        } catch (IOException e) {
            throw imposterize(e);
        }
    }

    private static Properties webappProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(JettyDriver.class.getResourceAsStream(WEBAPP_PROPERTIES_FILE));
        return properties;
    }

    public void start() throws Exception {
        configureJetty();
        bindWebApplication();
        bindDefaultHandler();
        startJetty();
    }

    public void stop() throws Exception {
        stopJetty();
    }

    private void configureJetty() throws Exception {
        configureThreadPool();
        configureConnector();
        configureHandlers();
        configureExtraOptions();
    }

    private void configureThreadPool() {
        QueuedThreadPool threadPool = new QueuedThreadPool(MAX_THREADS);
        jetty.setThreadPool(threadPool);
    }

    private void configureConnector() {
        Connector connector = new SelectChannelConnector();
        connector.setHost(serverHost());
        connector.setPort(serverPort());
        jetty.addConnector(connector);
    }

    private void configureHandlers() {
        jetty.setHandler(new HandlerList());
    }

    private void configureExtraOptions() {
        jetty.setGracefulShutdown(SHUTDOWN_TIMEOUT);
    }

    private void bindWebApplication() {
        WebAppContext appContext = new WebAppContext();
        appContext.setContextPath(contextPath());
        File warPath = new File(webappPath());
        appContext.setWar(warPath.getAbsolutePath());
        jetty.addHandler(appContext);
    }

    private void bindDefaultHandler() {
        jetty.addHandler(new DefaultHandler());
    }

    private void startJetty() throws Exception {
        jetty.start();
    }

    private void stopJetty() throws Exception {
        jetty.stop();
    }

    public void stopOnShutdown() {
        jetty.setStopAtShutdown(true);
    }
}
