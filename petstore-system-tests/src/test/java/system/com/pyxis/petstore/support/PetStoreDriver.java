package system.com.pyxis.petstore.support;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

public class PetStoreDriver {

    private final WebDriver webdriver;

    public PetStoreDriver() {
        this.webdriver = new ChromeDriver();
    }

    public <T extends PageObject> T navigateTo(Class<T> pageClass) throws Exception {
        webdriver.navigate().to(Routes.urlFor(pageClass));
        T page = PageFactory.initElements(webdriver, pageClass);
        page.assertOnRightPage();
        return page;
    }

    public void dispose() {
        webdriver.quit();
    }
}
