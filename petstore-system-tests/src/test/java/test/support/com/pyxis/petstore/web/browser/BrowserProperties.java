package test.support.com.pyxis.petstore.web.browser;

import test.support.com.pyxis.petstore.Properties;

public final class BrowserProperties {
    
    public static final String LIFECYCLE = "browser.lifecycle";

    private final Properties properties;

    public BrowserProperties(Properties properties) {
        this.properties = properties;
    }

    public String lifeCycle() {
        return System.getProperty(LIFECYCLE, properties.getValue(LIFECYCLE));
    }
}
