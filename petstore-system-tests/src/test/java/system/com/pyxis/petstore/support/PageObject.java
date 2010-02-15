package system.com.pyxis.petstore.support;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public abstract class PageObject {

    protected WebDriver webdriver;

    protected PageObject(WebDriver driver) {
        this.webdriver = driver;
    }

    public abstract void assertOnRightPage();

    public <T extends PageObject> T assertOn(Class<T> pageClass) {
        T page = PageFactory.initElements(webdriver, pageClass);
        page.assertOnRightPage();
        return page;
    }
}
