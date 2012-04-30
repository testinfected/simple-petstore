package test.support.com.pyxis.petstore.web.browser;

import org.openqa.selenium.Platform;
import org.testinfected.hamcrest.ExceptionImposter;
import test.support.com.pyxis.petstore.Properties;

import java.net.MalformedURLException;
import java.net.URL;

public final class BrowserProperties {
    
    public static final String LIFECYCLE = "browser.lifecycle";
    public static final String PROXY = "browser.proxy";
    public static final String NAME = "browser.name";
    public static final String VERSION = "browser.version";
    public static final String PLATFORM = "browser.platform";

    private final Properties properties;

    public BrowserProperties(Properties properties) {
        this.properties = properties;
    }

    public String lifeCycle() {
        return System.getProperty(LIFECYCLE, properties.getValue(LIFECYCLE));
    }

    public URL proxy() {
        try {
            return new URL(System.getProperty(PROXY, properties.getValue(PROXY)));
        } catch (MalformedURLException e) {
            throw ExceptionImposter.imposterize(e);
        }
    }

    public String name() {
        return System.getProperty(NAME, properties.getValue(NAME));
    }

    public String version() {
        return System.getProperty(VERSION, properties.getValue(VERSION));
    }

    public Platform platform() {
        return Platform.valueOf(System.getProperty(PLATFORM, properties.getValue(PLATFORM)));
    }
}
