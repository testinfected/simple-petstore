package test.system.com.pyxis.petstore.page;

import org.openqa.selenium.WebDriver;
import static org.openqa.selenium.lift.Finders.*;
import static org.openqa.selenium.lift.Matchers.*;

import static com.pyxis.matchers.selenium.SeleniumMatchers.*;

import test.support.com.pyxis.petstore.web.PageObject;

public class ItemsPage extends PageObject {

	public ItemsPage(WebDriver driver) {
		super(driver);
	}

	public void displaysItem(String number, String description, float price) {
/*		assertPresenceOf(cell().with(text(being(number))));
		assertPresenceOf(cell().with(text(being(description))));
		assertPresenceOf(cell().with(text(being(String.valueOf(price)))));
*/	}

}
