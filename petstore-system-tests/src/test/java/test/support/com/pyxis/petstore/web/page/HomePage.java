package test.support.com.pyxis.petstore.web.page;

import com.objogate.wl.web.AsyncWebDriver;

import static java.lang.String.valueOf;
import static org.hamcrest.Matchers.containsString;
import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.id;

public class HomePage extends Page {

    public HomePage(AsyncWebDriver browser) {
        super(browser);
    }

    public void searchFor(String keyword) {
        browser.element(id("keyword")).type(keyword);
        browser.element(id("search-button")).click();
	}

    public void showsCartIsEmpty() {
        browser.element(cssSelector("#shopping-cart")).assertText(containsString(valueOf(0)));
    }

    public void showsCartTotalQuantity(int quantity) {
        browser.element(cssSelector("#shopping-cart a")).assertText(containsString(valueOf(quantity)));
    }

    public void lookAtCartContent() {
        browser.element(cssSelector("#shopping-cart a")).click();
    }
}
