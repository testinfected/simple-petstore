package test.support.org.testinfected.petstore.web.drivers.pages;

import com.vtence.mario.BrowserDriver;
import org.openqa.selenium.By;

import static java.lang.String.valueOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.id;

public class CartPage extends Page {

    public CartPage(BrowserDriver browser) {
        super(browser);
    }

    public void showsItem(String itemNumber, String itemDescription, String unitPrice, int quantity, String totalPrice) {
        browser.element(cellShowingNameOf(itemNumber)).hasText(containsString(itemDescription));
        browser.element(cellShowingPriceOf(itemNumber)).hasText(equalTo(unitPrice));
        browser.element(cellShowingQuantityOf(itemNumber)).hasText(equalTo(valueOf(quantity)));
        browser.element(cellShowingTotalFor(itemNumber)).hasText(equalTo(totalPrice));
    }

    public CartPage showsGrandTotal(String price) {
        browser.element(id("cart-grand-total")).hasText(equalTo(price));
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


