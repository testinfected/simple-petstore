package test.support.org.testinfected.petstore.web.page;

import com.objogate.wl.web.AsyncWebDriver;
import com.vtence.molecule.support.HttpRequest;
import test.support.org.testinfected.petstore.web.AdministrationAPI;
import test.support.org.testinfected.petstore.web.TestEnvironment;
import test.support.org.testinfected.petstore.web.WebServer;

import static java.lang.String.valueOf;
import static org.hamcrest.Matchers.containsString;
import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.id;

public class PetStore {
    private final AsyncWebDriver browser;
    private final HttpRequest api;
    private final WebServer server;

    public static PetStore in(TestEnvironment environment) {
        return new PetStore(environment.fireBrowser(), environment.api(), new WebServer(environment.serverPort(), environment.webRoot()));
    }

    public PetStore(AsyncWebDriver browser, HttpRequest api, WebServer webServer) {
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

    public PetStore loginAs(String username) {
        return this;
    }

    public PetStore logout() {
        browser.element(id("logout")).click();
        return this;
    }

    public PetStore goToHomePage() {
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

    public AdministrationAPI administrate() {
        return new AdministrationAPI(api);
    }

    private String url(String path) {
        return "http://localhost:" + server.getPort() + path;
    }
}
