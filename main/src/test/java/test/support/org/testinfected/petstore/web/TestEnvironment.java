package test.support.org.testinfected.petstore.web;

import com.objogate.wl.UnsynchronizedProber;
import com.objogate.wl.web.AsyncWebDriver;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testinfected.petstore.Migrations;
import org.testinfected.petstore.util.PropertyFile;
import test.support.org.testinfected.molecule.integration.HttpRequest;
import test.support.org.testinfected.petstore.web.browser.Browser;
import test.support.org.testinfected.petstore.web.browser.Firefox;
import test.support.org.testinfected.petstore.web.browser.PhantomJS;
import test.support.org.testinfected.petstore.web.browser.RemoteBrowser;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static java.lang.Integer.parseInt;
import static test.support.org.testinfected.molecule.integration.HttpRequest.aRequest;

public class TestEnvironment {

    public static final String SERVER_PORT = "server.port";

    public static final String BROWSER_DRIVER = "browser.driver";
    public static final String BROWSER_REMOTE_URL = "browser.remote.url";
    public static final String BROWSER_REMOTE_CAPABILITY = "browser.capability.";

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
    private final Browser browser;

    public TestEnvironment(Properties properties) {
        this.properties = configure(properties);
        this.browser = selectBrowser();
    }

    private Properties configure(Properties defaults) {
        Properties props = new Properties();
        props.putAll(defaults);
        props.putAll(System.getProperties());
        return props;
    }

    private Browser selectBrowser() {
        String driver = asString(BROWSER_DRIVER);
        if (driver.equals("firefox")) return new Firefox(browserCapabilities());
        if (driver.equals("phantomjs")) return new PhantomJS(browserCapabilities());
        if (driver.equals("remote")) return new RemoteBrowser(asUrl(BROWSER_REMOTE_URL), browserCapabilities());
        throw new IllegalArgumentException(BROWSER_DRIVER + " should be one of firefox, phantomjs or remote, not: " + driver);
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

    public AsyncWebDriver openBrowser() {
        AsyncWebDriver browser = new AsyncWebDriver(new UnsynchronizedProber(), this.browser.launch());
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
