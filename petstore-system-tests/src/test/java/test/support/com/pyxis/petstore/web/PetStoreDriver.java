package test.support.com.pyxis.petstore.web;

import org.openqa.selenium.WebDriver;
import test.system.com.pyxis.petstore.page.HomePage;

public class PetStoreDriver {

    private final WebDriver webdriver;

    public PetStoreDriver() {
        webdriver = SharedInstanceWebDriverFactory.getInstance().newWebDriver();
    }

    public HomePage start() throws Exception {
        HomePage home = goToHomePage();
        home.logout();
        return home;
    }

    public HomePage goToHomePage() throws Exception {
        return navigateTo(HomePage.class);
    }

    public <T extends PageObject> T navigateTo(Class<T> pageClass) throws Exception {
        webdriver.navigate().to(Routes.urlFor(pageClass));
        return PageObject.aPage(webdriver, pageClass);
    }

    public void stop() {
        webdriver.quit();
    }
}
