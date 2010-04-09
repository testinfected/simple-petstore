package test.system.com.pyxis.petstore.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.lift.find.Finder;
import test.support.com.pyxis.petstore.web.PageObject;

import static com.pyxis.matchers.selenium.SeleniumMatchers.being;
import static com.pyxis.matchers.selenium.SeleniumMatchers.className;
import static org.openqa.selenium.lift.Finders.cell;
import static org.openqa.selenium.lift.Matchers.text;
import static test.support.com.pyxis.petstore.web.find.CssSelectorFinder.element;
import static test.support.com.pyxis.petstore.web.find.CssSelectorFinder.selector;

public class ItemsPage extends PageObject {

	public ItemsPage(WebDriver driver) {
		super(driver);
	}

	public void displaysItem(String number, String description, String price) {
		assertPresenceOf(cell().with(className("number")).with(text(being(number))));
		assertPresenceOf(cell().with(text(being(description))));
		assertPresenceOf(cell().with(className("price")).with(text(being(price))));
	}

    public void showsNoItemAvailable() {
        assertPresenceOf(element("out-of-stock"));
    }

    public CartPage addToCart(String itemNumber) {
        clickOn(addToCarButtonFor(itemNumber));
        return anInstanceOf(CartPage.class);
    }

    private Finder<WebElement, WebDriver> addToCarButtonFor(String itemNumber) {
        return selector(String.format("#add_to_cart_%s", itemNumber));
    }
}
