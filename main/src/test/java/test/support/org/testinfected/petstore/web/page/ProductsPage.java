package test.support.org.testinfected.petstore.web.page;

import com.objogate.wl.web.AsyncWebDriver;

import static org.hamcrest.core.StringContains.containsString;
import static org.testinfected.hamcrest.core.StringMatchers.being;
import static org.openqa.selenium.By.*;

public class ProductsPage extends Page {

    public ProductsPage(AsyncWebDriver browser) {
        super(browser);
    }

    public void showsNoResult() {
        browser.element(id("no-match")).assertExists();
    }

    public void displaysNumberOfResults(int matchCount) {
        browser.element(id("match-count")).assertText(being(matchCount));
    }

    public void displaysProduct(String number, String name) {
        browser.element(cssSelector("#product-" + number + " .product-name")).assertText(being(name));
    }

    public void browseItemsOf(String productName) {
        browser.element(linkText(productName)).click();
	}

    public void displays() {
        browser.assertTitle(containsString("Products"));
    }
}