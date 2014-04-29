package test.support.org.testinfected.petstore.web;

import com.objogate.wl.UnsynchronizedProber;
import com.objogate.wl.web.AsyncWebDriver;
import com.vtence.molecule.support.HttpRequest;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.DesiredCapabilities;
import test.support.org.testinfected.petstore.web.browser.Browser;
import test.support.org.testinfected.petstore.web.browser.Firefox;
import test.support.org.testinfected.petstore.web.browser.PhantomJS;
import test.support.org.testinfected.petstore.web.browser.RemoteBrowser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static java.lang.Integer.parseInt;

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
        try {
            InputStream config = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
            if (config == null) throw new IOException("Test propertieds not found: " + resource);
            Properties props = new Properties();
            props.load(config);
            return new TestEnvironment(props);
        } catch (IOException e) {
            throw new AssertionError(e);
        }
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
        String driver = getString(BROWSER_DRIVER);
        if (driver.equals("firefox")) return new Firefox(browserCapabilities());
        if (driver.equals("phantomjs")) return new PhantomJS(browserCapabilities());
        if (driver.equals("remote")) return new RemoteBrowser(getUrl(BROWSER_REMOTE_URL), browserCapabilities());
        throw new IllegalArgumentException(BROWSER_DRIVER + " should be one of firefox, phantomjs or remote, not: " + driver);
    }

    private Capabilities browserCapabilities() {
        Map<String, Object> capabilities = new HashMap<String, Object>();
        for (String property : properties.stringPropertyNames()) {
            if (isCapability(property)) {
                capabilities.put(capabilityName(property), toCapability(getString(property)));
            }
        }
        return new DesiredCapabilities(capabilities);
    }

    private boolean isCapability(String property) {
        return property.startsWith(BROWSER_REMOTE_CAPABILITY);
    }

    private String capabilityName(String property) {
        return property.substring(BROWSER_REMOTE_CAPABILITY.length());
    }

    private Object toCapability(String capability) {
        if (holdsMultipleValues(capability)) {
            return toCapabilityArray(capability);
        } else {
            return capability;
        }
    }

    private boolean holdsMultipleValues(String capability) {
        return capability.matches("\\[.+\\]");
    }

    private String[] toCapabilityArray(String capability) {
        return eliminateBlanks(capability.split("\\[|,\\s*|\\]"));
    }

    private String[] eliminateBlanks(String... capabilities) {
        List<String> meaningful = new ArrayList<String>();
        for (String cap : capabilities) {
            if (!isBlank(cap)) meaningful.add(cap);
        }
        return meaningful.toArray(new String[meaningful.size()]);
    }

    private boolean isBlank(String value) {
        return value.trim().isEmpty();
    }

    public AsyncWebDriver fireBrowser() {
        return new AsyncWebDriver(new UnsynchronizedProber(), this.browser.launch());
    }

    public HttpRequest api() {
        return new HttpRequest().onPort(serverPort()).withTimeOut(HTTP_TIMEOUT_IN_MILLIS);
    }

    public int serverPort() {
        return getInt(SERVER_PORT);
    }

    public File webRoot() {
        return WebRoot.locate();
    }

    private String getString(final String key) {
        return properties.getProperty(key);
    }

    private int getInt(final String key) {
        return parseInt(getString(key));
    }

    private URL getUrl(String key) {
        String url = getString(key);
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(key + " is not a valid url: " + url, e);
        }
    }
}
