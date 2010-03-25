package test.system.com.pyxis.petstore.page;

import org.openqa.selenium.WebDriver;
import test.support.com.pyxis.petstore.web.PageObject;

import static com.pyxis.matchers.selenium.SeleniumMatchers.id;
import static java.lang.String.valueOf;
import static org.hamcrest.Matchers.containsString;
import static org.openqa.selenium.lift.Finders.button;
import static org.openqa.selenium.lift.Finders.textbox;
import static org.openqa.selenium.lift.Matchers.text;
import static test.support.com.pyxis.petstore.web.find.CssSelectorFinder.element;
import static test.support.com.pyxis.petstore.web.find.CssSelectorFinder.selector;

public class HomePage extends PageObject {

    public HomePage(WebDriver driver) {
        super(driver);
    }

	public ProductsPage searchFor(String keyword) {
        type(keyword, into(textbox().with(id("keyword"))));
        clickOn(element("search"));
        return nowOn(ProductsPage.class);
	}

    public void showsCartIsEmpty() {
        assertNotPresent(selector("#shopping-cart a"));
    }

    public void showsCartTotalQuantity(int quantity) {
        assertPresenceOf(selector("#shopping-cart a").with(text(containsString(valueOf(quantity)))));
    }

    public void logout() {
        clickOn(button("Logout"));
    }
}
