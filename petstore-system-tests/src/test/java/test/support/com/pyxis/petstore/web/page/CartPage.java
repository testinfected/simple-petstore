package test.support.com.pyxis.petstore.web.page;

import com.objogate.wl.web.AsyncWebDriver;
import org.openqa.selenium.By;

import static com.pyxis.matchers.core.CoreMatchers.being;
import static org.hamcrest.Matchers.containsString;
import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.id;

public class CartPage extends Page {

    public CartPage(AsyncWebDriver browser) {
        super(browser);
    }

    public void showsItemInCart(String itemNumber, String itemDescription, String totalPrice) {
        browser.element(cellShowingNameOf(itemNumber)).assertText(containsString(itemDescription));
        browser.element(cellShowingTotalFor(itemNumber)).assertText(being(totalPrice));
    }

    public void showsItemQuantity(String itemNumber, int quantity) {
        browser.element(cellShowingQuantityOf(itemNumber)).assertText(being(quantity));
    }

    public void showsGrandTotal(String price) {
        browser.element(id("cart-grand-total")).assertText(being(price));
    }

    public void continueShopping() {
        browser.element(id("continue-shopping")).click();
    }

    private By cellShowingTotalFor(String itemNumber) {
        return cssSelector(domIdOf(itemNumber) + " td.total");
    }

    private By cellShowingQuantityOf(String itemNumber) {
        return cssSelector(domIdOf(itemNumber) + " td.count");
    }

    private By cellShowingNameOf(String itemNumber) {
        return cssSelector(domIdOf(itemNumber) + " td.text");
    }

    private String domIdOf(String itemNumber) {
        return "#cart-item-" + itemNumber;
    }

    public void checkout() {
        browser.element(cssSelector("#checkout a")).click();
    }
}


