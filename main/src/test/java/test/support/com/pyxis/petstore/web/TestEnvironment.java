package test.support.com.pyxis.petstore.web;

import com.objogate.wl.UnsynchronizedProber;
import com.objogate.wl.web.AsyncWebDriver;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testinfected.petstore.jdbc.DataSourceProperties;
import test.support.com.pyxis.petstore.web.browser.BrowserControl;
import test.support.com.pyxis.petstore.web.browser.LastingBrowser;
import test.support.com.pyxis.petstore.web.browser.PassingBrowser;
import test.support.com.pyxis.petstore.web.browser.RemoteBrowser;
import test.support.com.pyxis.petstore.web.server.ExternalServer;
import test.support.com.pyxis.petstore.web.server.LastingServer;
import test.support.com.pyxis.petstore.web.server.PassingServer;
import test.support.com.pyxis.petstore.web.server.ServerLifeCycle;
import test.support.com.pyxis.petstore.web.server.ServerSettings;
import org.testinfected.petstore.util.PropertyFile;
import test.support.com.pyxis.petstore.web.server.WebServer;
import test.support.org.testinfected.petstore.web.WebRoot;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static java.lang.Integer.parseInt;

public class TestEnvironment {

    public static final String SERVER_LIFECYCLE = "server.lifecycle";
    public static final String SERVER_SCHEME = "server.scheme";
    public static final String SERVER_HOST = "server.host";
    public static final String SERVER_PORT = "server.port";

    public static final String CONTEXT_PATH = "context.path";
    public static final String WEBAPP_PATH = "webapp.path";

    public static final String BROWSER_LIFECYCLE = "browser.lifecycle";
    public static final String BROWSER_REMOTE_URL = "browser.remote.url";
    public static final String BROWSER_REMOTE_CAPABILITY = "browser.remote.capability.";

    public static final String JDBC_URL = "jdbc.url";
    public static final String JDBC_USERNAME = "jdbc.username";
    public static final String JDBC_PASSWORD = "jdbc.password";

    private static final String TEST_PROPERTIES = "test.properties";

    private static TestEnvironment environment;

    public static TestEnvironment load() {
        if (environment == null) {
            environment = load(TEST_PROPERTIES);
        }
        return environment;
    }

    public static TestEnvironment load(String resource) {
        return new TestEnvironment(PropertyFile.load(resource));
    }

    private final Properties properties;
    private final ServerSettings serverSettings;
    private final ServerLifeCycle serverLifeCycle;
    private final BrowserControl browserControl;

    public TestEnvironment(Properties properties) {
        this.properties = configure(properties);
        this.serverSettings = readServerSettings();
        this.serverLifeCycle = selectServer();
        this.browserControl = selectBrowser();
    }

    private Properties configure(Properties defaults) {
        Properties props = new Properties();
        props.putAll(defaults);
        props.putAll(System.getProperties());
        return props;
    }

    private ServerSettings readServerSettings() {
        return new ServerSettings(
                asString(SERVER_SCHEME),
                asString(SERVER_HOST),
                asInt(SERVER_PORT),
                asString(CONTEXT_PATH),
                asString(WEBAPP_PATH));
    }

    private ServerLifeCycle selectServer() {
        String lifeCycle = asString(SERVER_LIFECYCLE);
        // new tests don't use a server lifecycle
        if (lifeCycle == null) return null;
        if (lifeCycle.equals("lasting")) return new LastingServer(serverSettings);
        if (lifeCycle.equals("passing")) return new PassingServer(serverSettings);
        if (lifeCycle.equals("external")) return new ExternalServer();
        throw new IllegalArgumentException(SERVER_LIFECYCLE + " should be one of lasting, passing, external: " + lifeCycle);
    }

    private BrowserControl selectBrowser() {
        String lifeCycle = asString(BROWSER_LIFECYCLE);
        if (lifeCycle.equals("lasting")) return new LastingBrowser();
        if (lifeCycle.equals("passing")) return new PassingBrowser();
        if (lifeCycle.equals("remote")) return new RemoteBrowser(asUrl(BROWSER_REMOTE_URL), browserCapabilities());
        throw new IllegalArgumentException(BROWSER_LIFECYCLE + " should be one of lasting, passing, remote: " + lifeCycle);
    }

    public Capabilities browserCapabilities() {
        Map<String, String> capabilities = new HashMap<String, String>();
        for (String property : properties.stringPropertyNames()) {
            if (isCapability(property)) {
                capabilities.put(capabilityName(property), asString(property));
            }
        }
        return new DesiredCapabilities(capabilities);
    }

    private String capabilityName(String property) {
        return property.substring(BROWSER_REMOTE_CAPABILITY.length());
    }

    private boolean isCapability(String property) {
        return property.startsWith(BROWSER_REMOTE_CAPABILITY);
    }

    public Properties getProperties() {
        return properties;
    }

    @Deprecated
    public ServerLifeCycle getServerLifeCycle() {
        return serverLifeCycle;
    }

    public BrowserControl browserControl() {
        return browserControl;
    }

    @Deprecated
    // todo remove database connection properties from the test environment
    public DataSourceProperties databaseProperties() {
        return new DataSourceProperties(asString(JDBC_URL), asString(JDBC_USERNAME), asString(JDBC_PASSWORD));
    }

    @Deprecated
    public Routing getRoutes() {
        return new Routing(serverBaseUrl());
    }

    private String serverBaseUrl() {
        return String.format("%s://%s:%s%s", serverSettings.scheme, serverSettings.host, serverSettings.port, serverSettings.contextPath);
    }

    public AsyncWebDriver launchBrowser() {
        AsyncWebDriver browser = new AsyncWebDriver(new UnsynchronizedProber(), browserControl.launch());
        browser.navigate().to("http://localhost:" + asInt(SERVER_PORT));
        return browser;
    }

    public WebServer startServer() throws Exception {
        WebServer server = new WebServer(asInt(SERVER_PORT), WebRoot.locate());
        server.start();
        return server;
    }

    private String asString(final String key) {
        return properties.getProperty(key);
    }

    private int asInt(final String key) {
        return parseInt(asString(key));
    }

    private URL asUrl(String key) {
        String url = asString(key);
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(key + " is not a valid url: " + url, e);
        }
    }
}
