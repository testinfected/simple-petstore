package test.support.org.testinfected.petstore.web.drivers.pages;

import com.objogate.wl.web.AsyncWebDriver;

public abstract class Page {

    protected final AsyncWebDriver browser;

    protected Page(AsyncWebDriver browser) {
        this.browser = browser;
    }
}
