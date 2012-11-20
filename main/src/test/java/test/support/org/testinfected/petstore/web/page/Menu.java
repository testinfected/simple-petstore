package test.support.org.testinfected.petstore.web.page;

import com.objogate.wl.web.AsyncWebDriver;

import static java.lang.String.valueOf;
import static org.hamcrest.Matchers.containsString;
import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.id;

public class Menu extends Page {

    public Menu(AsyncWebDriver browser) {
        super(browser);
    }

    public void search(String keyword) {
        browser.element(id("keyword")).type(keyword);
        browser.element(id("search")).click();
    }

    public void showsCartIsEmpty() {
        browser.element(cssSelector("#shopping-cart")).assertText(containsString(valueOf(0)));
    }

    public void showsCartTotalQuantity(int quantity) {
        browser.element(cssSelector("#shopping-cart a")).assertText(containsString(valueOf(quantity)));
    }

    public void logout() {
        browser.element(id("logout")).click();
    }
}
