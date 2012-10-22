package test.support.com.pyxis.petstore.web.page;

import com.objogate.wl.web.AsyncWebDriver;
import org.openqa.selenium.By;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.StringContains.containsString;
import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.id;

public class ItemsPage extends Page {

    public ItemsPage(AsyncWebDriver browser) {
        super(browser);
    }

	public void displaysItem(String number, String description, String price) {
        browser.element(itemNumber(number)).assertText(equalTo(number));
        browser.element(itemDescription(number)).assertText(equalTo(description));
        browser.element(itemPrice(number)).assertText(equalTo(price));
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

    public void showsNoItemAvailable() {
        browser.element(id("out-of-stock")).assertExists();
    }

    public void addToCart(String itemNumber) {
        browser.element(add(itemNumber)).click();
    }

    private String domIdOf(String itemNumber) {
        return "#item-" + itemNumber;
    }

    private By add(String itemNumber) {
        return cssSelector("#add-to-cart-" + itemNumber);
    }

    public void displays() {
        browser.assertTitle(containsString("PetStore - Items"));
    }
}
