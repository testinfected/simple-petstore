package test.system.com.pyxis.petstore.page;

import org.openqa.selenium.WebDriver;
import test.support.com.pyxis.petstore.web.PageObject;

import static com.pyxis.matchers.selenium.SeleniumMatchers.being;
import static com.pyxis.matchers.selenium.SeleniumMatchers.className;
import static org.hamcrest.Matchers.containsString;
import static org.openqa.selenium.lift.Finders.cell;
import static org.openqa.selenium.lift.Matchers.text;

public class CartPage extends PageObject {

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public void showsItemInCart(String itemDescription, int quantity) {
        assertPresenceOf(cell().with(className("item")).with(text(containsString(itemDescription))));
        assertPresenceOf(cell().with(className("count")).with(text(being(quantity))));
    }
}