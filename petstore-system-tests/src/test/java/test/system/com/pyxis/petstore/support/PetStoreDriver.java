package test.system.com.pyxis.petstore.support;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import test.system.com.pyxis.petstore.page.HomePage;

public class PetStoreDriver {

    private final WebDriver webdriver;

    public PetStoreDriver() {
        this.webdriver = new FirefoxDriver();
    }

    public HomePage start() throws Exception {
        return navigateTo(HomePage.class);
    }

    public <T extends PageObject> T navigateTo(Class<T> pageClass) throws Exception {
        webdriver.navigate().to(Routes.urlFor(pageClass));
        return PageObject.onPage(webdriver, pageClass);
    }

    public void close() {
        webdriver.close();
    }
}
