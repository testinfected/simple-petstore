package test.support.com.pyxis.petstore.web.page;

import com.objogate.wl.web.AsyncWebDriver;
import test.support.com.pyxis.petstore.web.Routing;

public abstract class Page {

    protected final AsyncWebDriver browser;

    protected Page(AsyncWebDriver browser) {
        this.browser = browser;
    }

    public void navigateThrough(Routing routes) {
        browser.navigate().to(routes.urlFor(getClass()));
    }
}
