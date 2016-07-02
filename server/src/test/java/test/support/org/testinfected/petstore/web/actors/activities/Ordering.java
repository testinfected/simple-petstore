package test.support.org.testinfected.petstore.web.actors.activities;

import test.support.org.testinfected.petstore.web.ScenarioContext;
import test.support.org.testinfected.petstore.web.drivers.ApplicationDriver;
import test.support.org.testinfected.petstore.web.drivers.pages.CartPage;
import test.support.org.testinfected.petstore.web.drivers.pages.CheckoutPage;
import test.support.org.testinfected.petstore.web.drivers.pages.ReceiptPage;
import test.system.org.testinfected.petstore.features.Item;

public class Ordering {
    private final ApplicationDriver application;
    private final ScenarioContext context;

    public Ordering(ApplicationDriver application, ScenarioContext context) {
        this.application = application;
        this.context = context;
    }

    public Ordering addToCart(String productName, String itemNumber) {
        application.search(productName).selectProduct(productName).addToCart(itemNumber);
        return this;
    }

    public Ordering seesTotalToPay(String amount) {
        application.openCart().checkout().showsTotalToPay(amount).continueShopping();
        return this;
    }

    public void seesCartContent(String totalPrice, Item... items) {
        CartPage cartPage = application.openCart();
        cartPage.showsGrandTotal(totalPrice);
        for (Item item : items) {
            cartPage.showsItem(item.number, item.description, item.price, item.quantity, item.totalPrice);
        }
    }

    public Ordering completeOrder(String firstName, String lastName, String email, String cardType, String cardNumber, String cardExpiryDate) {
        CheckoutPage checkoutPage = application.openCart().checkout();
        ReceiptPage receiptPage = checkoutPage.willBillTo(firstName, lastName, email)
                                              .willPayUsingCreditCard(cardType, cardNumber, cardExpiryDate)
                                              .confirm();
        context.set("orderNumber", receiptPage.getOrderNumber());
        receiptPage.continueShopping();
        return this;
    }

    public Ordering seesTotalPaid(String amount) {
        String orderNumber = context.get("orderNumber");
        application.openReceipt(orderNumber).showsTotalPaid(amount);
        return this;
    }

    public Ordering seesOrderedItems(Item... items) {
        String orderNumber = context.get("orderNumber");
        ReceiptPage receiptPage = application.openReceipt(orderNumber);
        for (Item item : items) {
            receiptPage.showsLineItem(item.number, item.description, item.totalPrice);
        }
        return this;
    }

    public Ordering seesCreditCardDetails(String cardType, String cardNumber, String cardExpiryDate) {
        String orderNumber = context.get("orderNumber");
        application.openReceipt(orderNumber).showsCreditCardDetails(cardType, cardNumber, cardExpiryDate);
        return this;
    }

    public Ordering seesBillingInformation(String firstName, String lastName, String emailAddress) {
        String orderNumber = context.get("orderNumber");
        application.openReceipt(orderNumber).showsBillingInformation(firstName, lastName, emailAddress);
        return this;
    }
}