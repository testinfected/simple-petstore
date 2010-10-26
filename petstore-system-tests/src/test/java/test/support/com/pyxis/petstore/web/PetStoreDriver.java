package test.support.com.pyxis.petstore.web;

import com.objogate.wl.UnsynchronizedProber;
import com.objogate.wl.web.AsyncWebDriver;
import org.openqa.selenium.WebDriver;
import test.support.com.pyxis.petstore.web.webdriver.WebDriverFactory;
import test.support.com.pyxis.petstore.web.page.HomePage;
import test.support.com.pyxis.petstore.web.page.Page;

public class PetStoreDriver {

    private final AsyncWebDriver browser;

    public PetStoreDriver() {
        browser = new AsyncWebDriver(new UnsynchronizedProber(), getWebDriver());
    }

    public HomePage start() throws Exception {
        HomePage home = Page.home(browser);
        home.go();
        home.logout();
        return home;
    }

    public void stop() {
        browser.quit();
    }

    private WebDriver getWebDriver() {
        return WebDriverFactory.getInstance().getWebDriver();
    }
}
