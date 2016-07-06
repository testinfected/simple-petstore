package test.support.org.testinfected.petstore.web.drivers;

import com.vtence.mario.BrowserDriver;
import test.support.org.testinfected.petstore.web.drivers.pages.CartPage;
import test.support.org.testinfected.petstore.web.drivers.pages.ProductsPage;
import test.support.org.testinfected.petstore.web.drivers.pages.ReceiptPage;

import static java.lang.String.valueOf;
import static org.hamcrest.Matchers.containsString;
import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.id;

public class ApplicationDriver {
    private final BrowserDriver browser;
    private final String serverUrl;

    public ApplicationDriver(BrowserDriver browser, String serverUrl) {
        this.browser = browser;
        this.serverUrl = serverUrl;
    }

    public ApplicationDriver enter() {
        browser.navigate().to(url("/"));
        return this;
    }

    public void leave() {
        browser.quit();
    }

    public ApplicationDriver loginAs(String username) {
        // There's no real login in that app
        return this;
    }

    public ApplicationDriver logout() {
        browser.element(id("logout")).click();
        return this;
    }

    public ProductsPage search(String keyword) {
        browser.element(id("keyword")).type(keyword);
        browser.element(id("search")).click();
        return new ProductsPage(browser);
    }

    public void displaysCartItemCount(int count) {
        browser.element(cssSelector("#shopping-cart")).hasText(containsString(String.format("(%s)", valueOf(count))));
    }

    public CartPage openCart() {
        browser.element(cssSelector("#shopping-cart a")).click();
        return new CartPage(browser);
    }

    public ReceiptPage openReceipt(String orderNumber) {
        browser.navigate().to(url("/orders/" + orderNumber));
        return new ReceiptPage(browser);
    }

    private String url(String path) {
        return serverUrl + path;
    }
}