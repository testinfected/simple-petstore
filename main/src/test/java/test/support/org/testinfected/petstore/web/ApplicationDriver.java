package test.support.org.testinfected.petstore.web;

import com.objogate.wl.web.AsyncWebDriver;
import test.support.org.testinfected.petstore.web.page.*;
import test.support.org.testinfected.petstore.web.page.OrderPage;

import java.io.IOException;
import java.math.BigDecimal;

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
    private CheckoutPage checkoutPage;
    private OrderPage orderPage;
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
        environment.clean();
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
        checkoutPage = new CheckoutPage(browser);
        orderPage = new OrderPage(browser);
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

    public void addProduct(String number, String name, String description, String photo) throws IOException {
        rest.addProduct(number, name, description, photo);
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

    public void showsItemQuantity(String itemNumber, int quantity) {
        cartPage.showsItemQuantity(itemNumber, quantity);
    }

    public void checkout() {
        cartPage.checkout();
        checkoutPage.displays();
    }

    public void showsTotalToPay(String total) {
        checkoutPage.showsTotalToPay(new BigDecimal(total));
    }

    public void pay(String firstName, String lastName, String email, String cardType, String cardNumber, String cardExpiryDate) {
        checkoutPage.willBillTo(firstName, lastName, email);
        checkoutPage.willPayUsingCreditCard(cardType, cardNumber, cardExpiryDate);
        checkoutPage.confirmOrder();
        orderPage.displays();
    }

    public void showsTotalPaid(String total) {
        orderPage.showsTotalPaid(new BigDecimal(total));
    }

    public void showsLineItem(String itemNumber, String itemDescription, String totalPrice) {
        orderPage.showsLineItem(itemNumber, itemDescription, totalPrice);
    }

    public void showsCreditCardDetails(String cardType, String cardNumber, String cardExpiryDate) {
        orderPage.showsCreditCardDetails(cardType, cardNumber, cardExpiryDate);
    }

    public void showsBillingInformation(String firstName, String lastName, String emailAddress) {
        orderPage.showsBillingInformation(firstName, lastName, emailAddress);
    }

    public void returnShopping() {
        orderPage.returnShopping();
        homePage.displays();
    }
}
