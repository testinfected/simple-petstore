package test.support.org.testinfected.petstore.web.page;

import com.objogate.wl.web.AsyncWebDriver;

import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.linkText;
import static org.testinfected.hamcrest.core.StringMatchers.being;

public class ProductsPage extends Page {

    public ProductsPage(AsyncWebDriver browser) {
        super(browser);
    }

    public void showsNoResult() {
        browser.element(id("no-match")).assertExists();
    }

    public ProductsPage displaysMatchCount(int matchCount) {
        browser.element(id("match-count")).assertText(being(matchCount));
        return this;
    }

    public ProductsPage displaysProduct(String number, String name) {
        browser.element(cssSelector("#product-" + number + " .product-name")).assertText(being(name));
        return this;
    }

    public ItemsPage selectProduct(String productName) {
        browser.element(linkText(productName)).click();
        return new ItemsPage(browser);
	}
}