package test.support.com.pyxis.petstore.web;

import com.objogate.wl.UnsynchronizedProber;
import com.objogate.wl.web.AsyncWebDriver;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testinfected.petstore.Migrations;
import org.testinfected.petstore.util.PropertyFile;
import test.support.com.pyxis.petstore.web.browser.BrowserControl;
import test.support.com.pyxis.petstore.web.browser.LastingBrowser;
import test.support.com.pyxis.petstore.web.browser.PassingBrowser;
import test.support.com.pyxis.petstore.web.browser.RemoteBrowser;
import test.support.com.pyxis.petstore.web.server.*;
import test.support.org.testinfected.petstore.web.HttpRequest;
import test.support.org.testinfected.petstore.web.WebRoot;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static java.lang.Integer.parseInt;
import static test.support.org.testinfected.petstore.web.HttpRequest.aRequest;

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

    private static final String TEST_PROPERTIES = "test.properties";
    private static final int HTTP_TIMEOUT_IN_MILLIS = 5000;

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
                serverPort(),
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

    private Capabilities browserCapabilities() {
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

    @Deprecated
    public BrowserControl browserControl() {
        return browserControl;
    }

    @Deprecated
    public Routing getRoutes() {
        return new Routing(serverBaseUrl());
    }

    private String serverBaseUrl() {
        return String.format("%s://%s:%s%s", serverSettings.scheme, serverSettings.host, serverSettings.port, serverSettings.contextPath);
    }

    public AsyncWebDriver openBrowser() {
        AsyncWebDriver browser = new AsyncWebDriver(new UnsynchronizedProber(), browserControl.launch());
        browser.navigate().to("http://localhost:" + serverPort());
        return browser;
    }

    public HttpRequest webClient() {
        return aRequest().onPort(serverPort()).withTimeOut(HTTP_TIMEOUT_IN_MILLIS);
    }

    public void clean() throws Exception {
        Migrations.main("-e", "test", "clean");
    }

    public int serverPort() {
        return asInt(SERVER_PORT);
    }

    public File webRoot() {
        return WebRoot.locate();
    }

    // todo move to PropertyFile
    private String asString(final String key) {
        return properties.getProperty(key);
    }

    // todo move to PropertyFile
    private int asInt(final String key) {
        return parseInt(asString(key));
    }

    // todo move to PropertyFile
    private URL asUrl(String key) {
        String url = asString(key);
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(key + " is not a valid url: " + url, e);
        }
    }
}
