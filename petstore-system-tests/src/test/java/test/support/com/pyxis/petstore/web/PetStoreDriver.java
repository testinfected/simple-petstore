package test.support.com.pyxis.petstore.web;

import com.objogate.wl.UnsynchronizedProber;
import com.objogate.wl.web.AsyncWebDriver;
import org.openqa.selenium.WebDriver;
import test.support.com.pyxis.petstore.web.page.*;
import test.support.com.pyxis.petstore.web.serverdriver.ServerDriver;
import test.support.com.pyxis.petstore.web.serverdriver.ServerDriverFactory;
import test.support.com.pyxis.petstore.web.webdriver.WebDriverFactory;

import java.math.BigDecimal;

import static test.support.com.pyxis.petstore.web.Routes.urlFor;
import static test.support.com.pyxis.petstore.web.serverdriver.AbstractServerDriverFactory.serverDriverFactory;
import static test.support.com.pyxis.petstore.web.webdriver.AbstractWebDriverFactory.webDriverFactory;

public class PetStoreDriver {

    private final ServerDriverFactory serverDriverFactory = serverDriverFactory();
    private final WebDriverFactory webDriverFactory = webDriverFactory();

    private ServerDriver serverDriver;
    private WebDriver webdriver;
    private AsyncWebDriver browser;

    private HomePage homePage;
    private ProductsPage productsPage;
    private ItemsPage itemsPage;
    private CartPage cartPage;
    private PurchasePage purchasePage;
    private ReceiptPage receiptPage;

    public void start() throws Exception {
        createDrivers();
        startBrowser();
    }

    private void createDrivers() throws Exception {
        createServerDriver();
        createWebDriver();
        createPageDrivers();
    }

    private void createServerDriver() throws Exception {
        serverDriver = serverDriverFactory.newServerDriver();
    }

    private void createWebDriver() {
        webdriver = webDriverFactory.newWebDriver();
        browser = new AsyncWebDriver(new UnsynchronizedProber(), webdriver);
    }

    private void createPageDrivers() {
        homePage = new HomePage(browser);
        productsPage = new ProductsPage(browser);
        itemsPage = new ItemsPage(browser);
        cartPage = new CartPage(browser);
        purchasePage = new PurchasePage(browser);
        receiptPage = new ReceiptPage(browser);
    }

    private void startBrowser() {
        browser.navigate().to(urlFor(HomePage.class));
        homePage.logout();
    }

    public void stop() throws Exception {
        webDriverFactory.disposeWebDriver(webdriver);
        serverDriverFactory.disposeServerDriver(serverDriver);
    }

    public void searchFor(String keyword) {
        homePage.searchFor(keyword);
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

    public void consultInventoryOf(String product) {
        searchFor(product);
        productsPage.browseItemsOf(product);
    }

    public void showsNoItemAvailable() {
        itemsPage.showsNoItemAvailable();
    }

    public void displaysItem(String number, String description, String price) {
        itemsPage.displaysItem(number, description, price);
    }

    public void buy(String product, String itemNumber) {
        consultInventoryOf(product);
        itemsPage.addToCart(itemNumber);
    }

    public void checkout() {
        cartPage.checkout();
    }

    public void showsCartIsEmpty() {
        homePage.showsCartIsEmpty();
    }

    public void showsCartTotalQuantity(int quantity) {
        homePage.showsCartTotalQuantity(quantity);
    }

    public void showsItemInCart(String itemNumber, String itemDescription, String totalPrice) {
        cartPage.showsItemInCart(itemNumber, itemDescription, totalPrice);
    }

    public void showsItemQuantity(String itemNumber, int quantity) {
        cartPage.showsItemQuantity(itemNumber, quantity);
    }

    public void showsGrandTotal(String price) {
        cartPage.showsGrandTotal(price);
    }

    public void showsTotalToPay(String total) {
        purchasePage.showsTotalToPay(new BigDecimal(total));
    }

    public void pay(String firstName, String lastName, String email, String cardType, String cardNumber, String cardExpiryDate) {
        purchasePage.willBillTo(firstName, lastName, email);
        purchasePage.willPayUsingCreditCard(cardType, cardNumber, cardExpiryDate);
        purchasePage.confirmOrder();
    }

    public void showsTotalPaid(String total) {
        receiptPage.showsTotalPaid(new BigDecimal(total));
    }

    public void showsLineItem(String itemNumber, String itemDescription, String totalPrice) {
        receiptPage.showsLineItem(itemNumber, itemDescription, totalPrice);
    }

    public void showsCreditCardDetails(String cardType, String cardNumber, String cardExpiryDate) {
        receiptPage.showsCreditCardDetails(cardType, cardNumber, cardExpiryDate);
    }

    public void showsBillingInformation(String firstName, String lastName, String emailAddress) {
        receiptPage.showsBillingInformation(firstName, lastName, emailAddress);
    }
}
