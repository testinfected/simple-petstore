package test.support.com.pyxis.petstore.web;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public abstract class PageObject {

    protected WebDriver webdriver;

    protected PageObject(WebDriver driver) {
        this.webdriver = driver;
    }

    public abstract void assertLocation();

    public <T extends PageObject> T nowOn(Class<T> pageClass) {
        return onPage(webdriver, pageClass);
    }

    public static <T extends PageObject> T onPage(WebDriver webdriver, Class<T> pageClass) {
        T page = PageFactory.initElements(webdriver, pageClass);
        page.assertLocation();
        return page;
    }
}
