package test.support.com.pyxis.petstore.web.browser;

import test.support.com.pyxis.petstore.Configuration;

public final class BrowserProperties {
    
    public static final String LIFECYCLE = "browser.lifecycle";

    private final Configuration configuration;

    public BrowserProperties(Configuration configuration) {
        this.configuration = configuration;
    }

    public String lifeCycle() {
        return System.getProperty(LIFECYCLE, configuration.getValue(LIFECYCLE));
    }
}
