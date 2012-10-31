package test.support.com.pyxis.petstore.web;

import com.objogate.wl.web.AsyncWebDriver;
import test.support.com.pyxis.petstore.web.page.CartPage;
import test.support.com.pyxis.petstore.web.page.HomePage;
import test.support.com.pyxis.petstore.web.page.ItemsPage;
import test.support.com.pyxis.petstore.web.page.Menu;
import test.support.com.pyxis.petstore.web.page.ProductsPage;
import test.support.com.pyxis.petstore.web.server.WebServer;

import java.io.IOException;

public class ApplicationDriver {

    private final WebServer server;
    private final ConsoleDriver console = new ConsoleDriver();

    private TestEnvironment environment;
    private RestDriver rest;
    private AsyncWebDriver browser;
    private HomePage homePage;
    private ProductsPage productsPage;
    private ItemsPage itemsPage;
    private CartPage cartPage;
    private Menu menu;

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
        itemsPage = new ItemsPage(browser);
        cartPage = new CartPage(browser);
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

    public void consultInventoryOf(String product) {
        searchFor(product);
        browseInventory(product);
    }

    public void browseInventory(String product) {
        productsPage.browseItemsOf(product);
        itemsPage.displays();
    }

    public void showsNoItemAvailable() {
        itemsPage.showsNoItemAvailable();
    }

    public void addItem(String productNumber, String itemNumber, String itemDescription, String itemPrice) throws IOException {
        rest.addItem(productNumber, itemNumber, itemDescription, itemPrice);
    }

    public void displaysItem(String number, String description, String price) {
        itemsPage.displaysItem(number, description, price);
    }

    public void returnToCatalog() {
        itemsPage.returnToCatalog();
    }

    public void showsCartIsEmpty() {
        menu.showsCartIsEmpty();
    }

    public void showsCartTotalQuantity(int quantity) {
        menu.showsCartTotalQuantity(quantity);
    }

    public void showsItemInCart(String itemNumber, String itemDescription, String totalPrice) {
        cartPage.showsItemInCart(itemNumber, itemDescription, totalPrice);
    }

    public void showsGrandTotal(String price) {
        cartPage.showsGrandTotal(price);
    }

    public void buy(String product, String itemNumber) {
        consultInventoryOf(product);
        buy(itemNumber);
    }

    public void buy(String itemNumber) {
        itemsPage.addToCart(itemNumber);
        cartPage.displays();
    }

    public void continueShopping() {
        cartPage.continueShopping();
        homePage.displays();
    }
}
