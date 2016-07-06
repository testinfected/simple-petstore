package test.support.org.testinfected.petstore.web.drivers.pages;

import com.vtence.mario.BrowserDriver;

import static java.lang.String.valueOf;
import static org.hamcrest.Matchers.equalTo;
import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.linkText;

public class ProductsPage extends Page {

    public ProductsPage(BrowserDriver browser) {
        super(browser);
    }

    public void showsNoResult() {
        browser.element(id("no-match")).exists();
    }

    public ProductsPage displaysMatchCount(int matchCount) {
        browser.element(id("match-count")).hasText(equalTo(valueOf(matchCount)));
        return this;
    }

    public ProductsPage displaysProduct(String number, String name) {
        browser.element(cssSelector("#product-" + number + " .product-name")).hasText(equalTo(name));
        return this;
    }

    public ItemsPage selectProduct(String productName) {
        browser.element(linkText(productName)).click();
        return new ItemsPage(browser);
	}
}