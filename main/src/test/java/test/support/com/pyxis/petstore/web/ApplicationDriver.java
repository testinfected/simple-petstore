package test.support.com.pyxis.petstore.web;

import com.objogate.wl.web.AsyncWebDriver;
import test.support.com.pyxis.petstore.builders.Builders;
import test.support.com.pyxis.petstore.builders.ProductBuilder;
import test.support.com.pyxis.petstore.web.browser.BrowserControl;
import test.support.com.pyxis.petstore.web.page.HomePage;
import test.support.com.pyxis.petstore.web.page.Menu;
import test.support.com.pyxis.petstore.web.page.ProductsPage;
import test.support.com.pyxis.petstore.web.server.WebServer;

public class ApplicationDriver {

    private final WebServer server;
    private final BrowserControl browserControl;
    private final DatabaseDriver database;

    private AsyncWebDriver browser;
    private HomePage homePage;
    private ProductsPage productsPage;
    private Menu menu;

    public ApplicationDriver(TestEnvironment environment) {
        this.server = new WebServer(environment.getServerPort(), environment.getWebRoot());
        this.database = DatabaseDriver.configure(environment.getDatabaseConfiguration());
        this.browserControl = environment.getBrowserControl();
    }

    public void start() throws Exception {
        startDatabase();
        startWebServer();
        startBrowser();
        openHomePage();
    }

    private void startDatabase() throws Exception {
        database.migrate();
        database.clean();
        database.connect();
    }

    private void startWebServer() throws Exception {
        server.start();
    }

    private void startBrowser() {
        browser = browserControl.launch();
        menu = new Menu(browser);
        homePage = new HomePage(browser);
        productsPage = new ProductsPage(browser);
    }

    public void openHomePage() {
        browser.navigate().to(server.getUrl());
    }

    public void stop() throws Exception {
        logout();
        server.stop();
        browser.quit();
        database.stop();
    }

    public void logout() {
        menu.logout();
        homePage.displays();
    }

    public void searchFor(String keyword) {
        menu.search(keyword);
        productsPage.displays();
    }

    public void showsNoResult() {
        productsPage.showsNoResult();
    }

    public void displaysNumberOfResults(int matchCount) {
        productsPage.displaysNumberOfResults(matchCount);
    }

    public void displaysProduct(String number, String name) {
        productsPage.displaysProduct(number, name);
    }

    public void addProducts(ProductBuilder... products) throws Exception {
        database.addProducts(Builders.build(products));
    }
}
