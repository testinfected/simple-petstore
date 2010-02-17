package system.com.pyxis.petstore.support;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import system.com.pyxis.petstore.page.HomePage;

public class PetStoreDriver {

    private final WebDriver webdriver;

    public PetStoreDriver() {
        this.webdriver = new ChromeDriver();
    }

    public HomePage start() throws Exception {
        return navigateTo(HomePage.class);
    }

    public <T extends PageObject> T navigateTo(Class<T> pageClass) throws Exception {
        webdriver.navigate().to(Routes.urlFor(pageClass));
        T page = PageObject.newPage(webdriver, pageClass);
        page.assertLocation();
        return page;
    }

    public void close() {
        webdriver.close();
    }
}
