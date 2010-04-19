package test.system.com.pyxis.petstore.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.lift.find.Finder;
import test.support.com.pyxis.petstore.web.PageObject;

import static com.pyxis.matchers.selenium.SeleniumMatchers.being;
import static com.pyxis.matchers.selenium.SeleniumMatchers.id;
import static org.hamcrest.Matchers.containsString;
import static org.openqa.selenium.lift.Finders.link;
import static org.openqa.selenium.lift.Matchers.text;
import static test.support.com.pyxis.petstore.web.find.CssSelectorFinder.element;
import static test.support.com.pyxis.petstore.web.find.CssSelectorFinder.selector;

public class CartPage extends PageObject {

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public void showsItemInCart(String itemNumber, String itemDescription, String totalPrice) {
        assertPresenceOf(cellShowingNameFor(itemNumber).with(text(containsString(itemDescription))));
        assertPresenceOf(cellShowingTotalFor(itemNumber).with(text(being(totalPrice))));
    }

    public void showsItemQuantity(String itemNumber, int quantity) {
        assertPresenceOf(cellShowingQuantityFor(itemNumber).with(text(being(quantity))));
    }

    public void showsGrandTotal(String price) {
        assertPresenceOf(element("cart-grand-total").with(text(being(price))));
    }

    public ItemsPage continueShopping() {
        clickOn(link().with(id("continue-shopping")));
        return anInstanceOf(ItemsPage.class);
    }

    private Finder<WebElement, WebDriver> cellShowingTotalFor(String itemNumber) {
        return selector(domIdOf(itemNumber) + " td.total");
    }

    private Finder<WebElement, WebDriver> cellShowingQuantityFor(String itemNumber) {
        return selector(domIdOf(itemNumber) + " td.count");
    }

    private Finder<WebElement, WebDriver> cellShowingNameFor(String itemNumber) {
        return selector(domIdOf(itemNumber) + " td.text");
    }

    private String domIdOf(String itemNumber) {
        return "#cart-item-" + itemNumber;
    }

    public PurchasePage checkout() {
        clickOn(link().with(id("checkout")));
        return anInstanceOf(PurchasePage.class);
    }
}


