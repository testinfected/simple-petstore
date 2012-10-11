package test.support.com.pyxis.petstore.web;

import com.objogate.wl.web.AsyncWebDriver;
import test.support.com.pyxis.petstore.web.page.HomePage;
import test.support.com.pyxis.petstore.web.page.Menu;
import test.support.com.pyxis.petstore.web.page.ProductsPage;
import test.support.com.pyxis.petstore.web.server.WebServer;

import java.io.IOException;

public class ApplicationDriver {

    private final WebServer server;
    private final ConsoleDriver console = new ConsoleDriver();

    private RestDriver rest;
    private AsyncWebDriver browser;
    private HomePage homePage;
    private ProductsPage productsPage;
    private Menu menu;
    private TestEnvironment environment;

    public ApplicationDriver(TestEnvironment environment) {
        this.environment = environment;
        this.server = new WebServer(environment.serverPort(), environment.webRoot());
        this.rest = new RestDriver(environment.webClient());
    }

    public void start() throws Exception {
        suppressConsoleOutput();
        cleanupEnvironment();
        startServer();
        openBrowser();
        makeDrivers();
    }

    private void suppressConsoleOutput() {
        console.capture();
    }

    private void cleanupEnvironment() throws Exception {
        environment.cleanUp();
    }

    private void startServer() throws Exception {
        server.start();
    }

    private void openBrowser() {
        browser = environment.openBrowser();
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
        restoreConsoleOutput();
    }

    private void restoreConsoleOutput() {
        console.release();
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

    public void addProduct(String number, String name) throws IOException {
        addProduct(number, name, "");
    }

    public void addProduct(String number, String name, String description) throws IOException {
        rest.addProduct(number, name, description);
    }
}
