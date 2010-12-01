package test.support.com.pyxis.petstore.web;

import com.objogate.wl.UnsynchronizedProber;
import com.objogate.wl.web.AsyncWebDriver;
import org.openqa.selenium.WebDriver;
import test.support.com.pyxis.petstore.web.page.*;
import test.support.com.pyxis.petstore.web.webdriver.WebDriverFactory;

import java.math.BigDecimal;

import static org.openqa.selenium.By.id;
import static test.support.com.pyxis.petstore.web.Routes.urlFor;

public class PetStoreDriver {

    private final AsyncWebDriver browser = new AsyncWebDriver(new UnsynchronizedProber(), getWebDriver());

    private final HomePage homePage = new HomePage(browser);
    private final ProductsPage productsPage = new ProductsPage(browser);
    private final ItemsPage itemsPage = new ItemsPage(browser);
    private final CartPage cartPage = new CartPage(browser);
    private final PurchasePage purchasePage = new PurchasePage(browser);
    private final ReceiptPage receiptPage = new ReceiptPage(browser);

    public void start() throws Exception {
        browser.navigate().to(urlFor(HomePage.class));
        clearSession();
    }

    public void stop() {
        browser.quit();
    }

    public void searchFor(String keyword) {
        homePage.searchFor(keyword);
    }

    public void showsCartIsEmpty() {
        homePage.showsCartIsEmpty();
    }

    public void showsCartTotalQuantity(int quantity) {
        homePage.showsCartTotalQuantity(quantity);
    }

    public void viewCart() {
        homePage.lookAtCartContent();
    }

    public void clearSession() {
        browser.element(id("logout")).click();
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

    public void browseItemsOf(String productName) {
        productsPage.browseItemsOf(productName);
    }

    public void displaysItem(String number, String description, String price) {
        itemsPage.displaysItem(number, description, price);
    }

    public void showsNoItemAvailable() {
        itemsPage.showsNoItemAvailable();
    }

    public void addToCart(String itemNumber) {
        itemsPage.addToCart(itemNumber);
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

    public void continueShopping() {
        cartPage.continueShopping();
    }

    public void checkout() {
        cartPage.checkout();
    }

    public void showsTotalToPay(BigDecimal total) {
        purchasePage.showsTotalToPay(total);
    }

    public void billTo(String firstName, String lastName, String email) {
        purchasePage.willBillTo(firstName, lastName, email);
    }

    public void payUsingCreditCard(String cardType, String cardNumber, String expiryDate) {
        purchasePage.willPayUsingCreditCard(cardType, cardNumber, expiryDate);
    }

    public void confirmOrder() {
        purchasePage.confirmOrder();
    }

    public void showsTotalPaid(BigDecimal total) {
        receiptPage.showsTotalPaid(total);
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

    public void returnShopping() {
        receiptPage.continueShopping();
    }

    private WebDriver getWebDriver() {
        return WebDriverFactory.getInstance().getWebDriver();
    }
}
