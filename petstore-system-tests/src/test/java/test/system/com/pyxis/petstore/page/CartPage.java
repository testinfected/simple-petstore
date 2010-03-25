package test.system.com.pyxis.petstore.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.lift.find.Finder;
import test.support.com.pyxis.petstore.web.PageObject;

import static com.pyxis.matchers.selenium.SeleniumMatchers.being;
import static org.hamcrest.Matchers.containsString;
import static org.openqa.selenium.lift.Matchers.text;
import static test.support.com.pyxis.petstore.web.find.CssSelectorFinder.selector;

public class CartPage extends PageObject {

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public void showsItemInCart(String itemNumber, String itemDescription, int quantity, String totalPrice) {
        assertPresenceOf(cellShowingNameFor(itemNumber).with(text(containsString(itemDescription))));
        assertPresenceOf(cellShowingQuantityFor(itemNumber).with(text(being(quantity))));
        assertPresenceOf(cellShowingTotalFor(itemNumber).with(text(being(totalPrice))));
    }

    private Finder<WebElement, WebDriver> cellShowingTotalFor(String itemNumber) {
        return selector(domIdOf(itemNumber) + " td.total");
    }

    private Finder<WebElement, WebDriver> cellShowingQuantityFor(String itemNumber) {
        return selector(domIdOf(itemNumber) + " td.count");
    }

    private Finder<WebElement, WebDriver> cellShowingNameFor(String itemNumber) {
        return selector(domIdOf(itemNumber) + " td.item");
    }

    private String domIdOf(String itemNumber) {
        return "#cart_item_" + itemNumber;
    }
}


