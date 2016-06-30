package test.support.org.testinfected.petstore.web.drivers.pages;

import com.objogate.wl.web.AsyncWebDriver;
import com.vtence.molecule.testing.http.HttpRequest;
import test.support.org.testinfected.petstore.web.TestEnvironment;
import test.support.org.testinfected.petstore.web.drivers.APIDriver;
import test.support.org.testinfected.petstore.web.drivers.ServerDriver;

import static java.lang.String.valueOf;
import static org.hamcrest.Matchers.containsString;
import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.id;

public class PetStoreDriver {
    private final AsyncWebDriver browser;
    private final HttpRequest api;
    private final ServerDriver server;

    public static PetStoreDriver in(TestEnvironment environment) {
        return new PetStoreDriver(environment.fireBrowser(), environment.api(), new ServerDriver(environment.serverPort(), environment.webRoot()));
    }

    public PetStoreDriver(AsyncWebDriver browser, HttpRequest api, ServerDriver webServer) {
        this.browser = browser;
        this.api = api;
        this.server = webServer;
    }

    public void start() throws Exception {
        startServer();
    }

    public void stop() throws Exception {
        stopServer();
        stopBrowser();
    }

    private void startServer() throws Exception {
        server.start();
    }

    private void stopBrowser() {
        browser.quit();
    }

    private void stopServer() throws Exception {
        server.stop();
    }

    public boolean isLoggedIn(String username) {
        return false;
    }

    public PetStoreDriver loginAs(String username) {
        return this;
    }

    public PetStoreDriver logout() {
        browser.element(id("logout")).click();
        return this;
    }

    public PetStoreDriver goToHomePage() {
        browser.navigate().to(url("/"));
        return this;
    }

    public ProductsPage search(String keyword) {
        browser.element(id("keyword")).type(keyword);
        browser.element(id("search")).click();
        return new ProductsPage(browser);
    }

    public void displaysCartItemCount(int count) {
        browser.element(cssSelector("#shopping-cart")).assertText(containsString(String.format("(%s)", valueOf(count))));
    }

    public CartPage goToCartPage() {
        browser.element(cssSelector("#shopping-cart a")).click();
        return new CartPage(browser);
    }

    public ReceiptPage goToReceiptPage(String orderNumber) {
        browser.navigate().to(url("/orders/" + orderNumber));
        return new ReceiptPage(browser);
    }

    public APIDriver administrate() {
        return new APIDriver(api);
    }

    private String url(String path) {
        return "http://localhost:" + server.getPort() + path;
    }
}
