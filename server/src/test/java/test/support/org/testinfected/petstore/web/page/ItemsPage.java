package test.support.org.testinfected.petstore.web.page;

import com.objogate.wl.web.AsyncWebDriver;
import org.openqa.selenium.By;

import static org.hamcrest.Matchers.equalTo;
import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.id;

public class ItemsPage extends Page {

    public ItemsPage(AsyncWebDriver browser) {
        super(browser);
    }

	public ItemsPage displaysItem(String number, String description, String price) {
        browser.element(itemNumber(number)).assertText(equalTo(number));
        browser.element(itemDescription(number)).assertText(equalTo(description));
        browser.element(itemPrice(number)).assertText(equalTo(price));
        return this;
	}

    private By itemPrice(String number) {
        return cssSelector(domIdOf(number) + " .price");
    }

    private By itemDescription(String number) {
        return cssSelector(domIdOf(number) + " .description");
    }

    private By itemNumber(String number) {
        return cssSelector(domIdOf(number) + " .number");
    }

    public ItemsPage showsNoItemAvailable() {
        browser.element(id("out-of-stock")).assertExists();
        return this;
    }

    public CartPage addToCart(String itemNumber) {
        browser.element(add(itemNumber)).click();
        return new CartPage(browser);
    }

    private String domIdOf(String itemNumber) {
        return "#item-" + itemNumber;
    }

    private By add(String itemNumber) {
        return cssSelector("#add-to-cart-" + itemNumber);
    }

    public void returnToCatalog() {
        browser.element(id("continue-shopping")).click();
    }
}
