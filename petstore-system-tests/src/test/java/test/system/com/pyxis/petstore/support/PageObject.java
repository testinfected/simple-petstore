package test.system.com.pyxis.petstore.support;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public abstract class PageObject {

    protected WebDriver webdriver;

    protected PageObject(WebDriver driver) {
        this.webdriver = driver;
    }

    public abstract void assertLocation();

    public <T extends PageObject> T nowOn(Class<T> pageClass) {
        T page = newPage(webdriver, pageClass);
        page.assertLocation();
        return page;
    }

    public static <T extends PageObject> T newPage(WebDriver webdriver, Class<T> pageClass) {
        return PageFactory.initElements(webdriver, pageClass);
    }
}
