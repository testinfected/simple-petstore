package test.support.org.testinfected.petstore.web;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testinfected.petstore.lib.Configuration;
import test.support.org.testinfected.petstore.web.drivers.browsers.Browser;
import test.support.org.testinfected.petstore.web.drivers.browsers.Firefox;
import test.support.org.testinfected.petstore.web.drivers.browsers.PhantomJS;
import test.support.org.testinfected.petstore.web.drivers.browsers.RemoteBrowser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestSettings {

    private static final String TEST_PROPERTIES = "test.properties";

    public static TestSettings load() {
        try {
            Configuration config = Configuration.load(TEST_PROPERTIES);
            config.merge(System.getProperties());
            return new TestSettings(config);
        } catch (IOException e) {
            throw new AssertionError("Failed to load test settings", e);
        }
    }

    private static final String SERVER_PORT = "server.port";
    private static final String BROWSER_DRIVER = "browser.driver";
    private static final String BROWSER_REMOTE_URL = "browser.remote.url";
    private static final String BROWSER_REMOTE_CAPABILITY = "browser.capability.";
    private static final String POLL_DELAY = "poll.delay";
    private static final String POLL_TIMEOUT = "poll.timeout";

    public final int serverPort;
    public final String serverUrl;
    public final File webRoot;
    public final Browser browser;
    public final long pollTimeout;
    public final long pollDelay;

    public TestSettings(Configuration config) {
        this.serverPort = config.getInt(SERVER_PORT);
        this.serverUrl = "http://127.0.0.1:" + serverPort;
        this.webRoot = WebRoot.locate();
        this.browser = selectBrowser(config);
        this.pollTimeout = config.getInt(POLL_TIMEOUT, 2000);
        this.pollDelay = config.getInt(POLL_DELAY, 50);
    }

    private Browser selectBrowser(Configuration config) {
        String driver = config.get(BROWSER_DRIVER);
        Capabilities capabilities = browserCapabilities(config);
        if (driver.equals("firefox")) return new Firefox(capabilities);
        if (driver.equals("phantomjs")) return new PhantomJS(capabilities);
        if (driver.equals("remote")) return new RemoteBrowser(config.getURL(BROWSER_REMOTE_URL), capabilities);
        throw new IllegalArgumentException(BROWSER_DRIVER + " should be one of firefox, phantomjs or remote, not: " + driver);
    }

    private Capabilities browserCapabilities(Configuration config) {
        Map<String, Object> capabilities = new HashMap<>();
        for (String key : config.keys()) {
            if (isCapability(key)) {
                capabilities.put(capabilityName(key), toCapability(config.get(key)));
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
        List<String> meaningful = new ArrayList<>();
        for (String cap : capabilities) {
            if (!isBlank(cap)) meaningful.add(cap);
        }
        return meaningful.toArray(new String[meaningful.size()]);
    }

    private boolean isBlank(String value) {
        return value.trim().isEmpty();
    }
}
