package test.support.org.testinfected.petstore.web.page;

import com.objogate.wl.web.AsyncWebDriver;

import static org.hamcrest.Matchers.containsString;

public class HomePage extends Page {

    public HomePage(AsyncWebDriver browser) {
        super(browser);
    }

    public void displays() {
        browser.assertTitle(containsString("Home"));
    }
}
