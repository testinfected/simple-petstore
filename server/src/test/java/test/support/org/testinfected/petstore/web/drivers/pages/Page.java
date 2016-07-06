package test.support.org.testinfected.petstore.web.drivers.pages;

import com.vtence.mario.BrowserDriver;

public abstract class Page {

    protected final BrowserDriver browser;

    protected Page(BrowserDriver browser) {
        this.browser = browser;
    }
}
