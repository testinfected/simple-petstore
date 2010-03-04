package test.system.com.pyxis.petstore.page;

import org.openqa.selenium.WebDriver;
import test.support.com.pyxis.petstore.web.PageObject;

import static org.openqa.selenium.lift.Finders.textbox;
import static com.pyxis.matchers.selenium.SeleniumMatchers.id;
import static test.support.com.pyxis.petstore.web.find.ButtonFinder.button;

public class HomePage extends PageObject {

    public HomePage(WebDriver driver) {
        super(driver);
    }

	public ProductsPage searchFor(String keyword) {
        type(keyword, into(textbox().with(id("keyword"))));
        clickOn(button());
        return nowOn(ProductsPage.class);
	}

}
