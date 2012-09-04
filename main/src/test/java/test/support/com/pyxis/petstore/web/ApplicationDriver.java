package test.support.com.pyxis.petstore.web;

import com.objogate.wl.web.AsyncWebDriver;
import test.support.com.pyxis.petstore.builders.Builders;
import test.support.com.pyxis.petstore.builders.ProductBuilder;
import test.support.com.pyxis.petstore.web.page.HomePage;
import test.support.com.pyxis.petstore.web.page.Menu;
import test.support.com.pyxis.petstore.web.page.ProductsPage;
import test.support.com.pyxis.petstore.web.server.WebServer;

import java.sql.SQLException;

public class ApplicationDriver {

    private final DatabaseDriver database;

    private WebServer server;
    private AsyncWebDriver browser;
    private HomePage homePage;
    private ProductsPage productsPage;
    private Menu menu;
    private TestEnvironment environment;

    public ApplicationDriver(TestEnvironment environment) {
        this.environment = environment;
        // todo don't go backdoor to the database, use a REST service
        this.database = DatabaseDriver.create(environment.databaseProperties());
    }

    public void start() throws Exception {
        startDatabase();
        startServer();
        launchBrowser();
        makeDrivers();
    }

    private void startDatabase() throws Exception {
        database.migrate();
        database.clean();
        database.connect();
    }

    private void startServer() throws Exception {
        server = environment.startServer();
    }

    private void launchBrowser() {
        browser = environment.launchBrowser();
    }

    private void makeDrivers() {
        menu = new Menu(browser);
        homePage = new HomePage(browser);
        productsPage = new ProductsPage(browser);
    }

    public void stop() throws Exception {
        logout();
        stopServer();
        stopBrowser();
        stopDatabase();
    }

    private void stopDatabase() throws SQLException {
        database.stop();
    }

    private void stopBrowser() {
        browser.quit();
    }

    private void stopServer() throws Exception {
        server.stop();
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
