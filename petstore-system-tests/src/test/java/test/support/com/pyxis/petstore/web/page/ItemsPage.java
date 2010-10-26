package test.support.com.pyxis.petstore.web.page;

import com.objogate.wl.web.AsyncWebDriver;
import org.openqa.selenium.By;

import static com.pyxis.matchers.core.CoreMatchers.being;
import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.id;

public class ItemsPage extends Page {
    public ItemsPage(AsyncWebDriver browser) {
        super(browser);
    }

	public void displaysItem(String number, String description, String price) {
        browser.element(numberOfItem(number)).assertText(being(number));
        browser.element(descriptionOfItem(number)).assertText(being(description));
        browser.element(priceOfItem(number)).assertText(being(price));
	}

    private By priceOfItem(String number) {
        return cssSelector(domIdOf(number) + " .price");
    }

    private By descriptionOfItem(String number) {
        return cssSelector(domIdOf(number) + " .description");
    }

    private By numberOfItem(String number) {
        return cssSelector(domIdOf(number) + " .number");
    }

    public void showsNoItemAvailable() {
        browser.element(id("out-of-stock")).assertExists();
    }

    public CartPage addToCart(String itemNumber) {
        browser.element(add(itemNumber)).click();
        return cartPage();
    }

    private String domIdOf(String itemNumber) {
        return "#item-" + itemNumber;
    }

    private By add(String itemNumber) {
        return cssSelector("#add-to-cart-" + itemNumber);
    }
}
