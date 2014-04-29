package test.support.org.testinfected.petstore.web.page;

import com.objogate.wl.web.AsyncWebDriver;
import org.openqa.selenium.By;

import static org.hamcrest.Matchers.containsString;
import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.id;
import static org.testinfected.hamcrest.core.StringMatchers.being;

public class CartPage extends Page {

    public CartPage(AsyncWebDriver browser) {
        super(browser);
    }

    public void showsItem(String itemNumber, String itemDescription, String unitPrice, int quantity, String totalPrice) {
        browser.element(cellShowingNameOf(itemNumber)).assertText(containsString(itemDescription));
        browser.element(cellShowingPriceOf(itemNumber)).assertText(being(unitPrice));
        browser.element(cellShowingQuantityOf(itemNumber)).assertText(being(quantity));
        browser.element(cellShowingTotalFor(itemNumber)).assertText(being(totalPrice));
    }

    public CartPage showsGrandTotal(String price) {
        browser.element(id("cart-grand-total")).assertText(being(price));
        return this;
    }

    public CheckoutPage checkout() {
        browser.element(cssSelector(".actions .confirm a")).click();
        return new CheckoutPage(browser);
    }

    public void continueShopping() {
        browser.element(cssSelector(".actions .cancel")).click();
    }

    private By cellShowingNameOf(String itemNumber) {
        return cssSelector(domIdOf(itemNumber) + " td.text");
    }

    private By cellShowingPriceOf(String itemNumber) {
        return cssSelector(domIdOf(itemNumber) + " td.price");
    }

    private By cellShowingQuantityOf(String itemNumber) {
        return cssSelector(domIdOf(itemNumber) + " td.count");
    }

    private By cellShowingTotalFor(String itemNumber) {
        return cssSelector(domIdOf(itemNumber) + " td.total");
    }

    private String domIdOf(String itemNumber) {
        return "#cart-item-" + itemNumber;
    }
}


