package test.system.com.pyxis.petstore.page;

import org.openqa.selenium.WebDriver;
import test.support.com.pyxis.petstore.web.PageObject;

import static com.pyxis.matchers.selenium.SeleniumMatchers.being;
import static com.pyxis.matchers.selenium.SeleniumMatchers.className;
import static org.openqa.selenium.lift.Finders.cell;
import static org.openqa.selenium.lift.Finders.link;
import static org.openqa.selenium.lift.Matchers.text;
import static test.support.com.pyxis.petstore.web.find.CssSelectorFinder.element;

public class ProductsPage extends PageObject {

    public ProductsPage(WebDriver webDriver) {
        super(webDriver);
    }

    public void displaysNoResult() {
        assertPresenceOf(element("no-match"));
    }

    public void displaysNumberOfResults(int matchCount) {
        assertPresenceOf(element("match-count").with(text(being(matchCount))));
    }

    public void displaysProduct(String productNumber, String productName) {
        assertPresenceOf(cell().with(className("number")).with(text(being(productNumber))));
        assertPresenceOf(cell().with(className("name")).with(text(being(productName))));
    }

    public ItemsPage browseItemsOf(String productName) {
		clickOn(link().with(text(being(productName))));
		return nowOn(ItemsPage.class);
	}
}