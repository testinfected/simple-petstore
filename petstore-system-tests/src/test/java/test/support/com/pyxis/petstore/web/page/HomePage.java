package test.support.com.pyxis.petstore.web.page;

import com.objogate.wl.web.AsyncWebDriver;

import static org.hamcrest.Matchers.equalTo;

public class HomePage extends Page {

    public HomePage(AsyncWebDriver browser) {
        super(browser);
    }

    public void displays() {
        browser.assertTitle(equalTo("PetStore"));
    }
}
