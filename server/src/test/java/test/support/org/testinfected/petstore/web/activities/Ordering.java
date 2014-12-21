package test.support.org.testinfected.petstore.web.activities;

import test.support.org.testinfected.petstore.web.page.CheckoutPage;
import test.support.org.testinfected.petstore.web.page.PetStoreDriver;
import test.support.org.testinfected.petstore.web.page.ReceiptPage;
import test.system.org.testinfected.petstore.features.Item;

public class Ordering {
    private final PetStoreDriver petstore;
    private String orderNumber;

    public Ordering(PetStoreDriver petstore) {
        this.petstore = petstore;
    }

    public void amountsTo(String amount) {
        petstore.goToCartPage().checkout().showsTotalToPay(amount).continueShopping();
    }

    public void confirm(String firstName, String lastName, String email, String cardType, String cardNumber, String cardExpiryDate) {
        CheckoutPage checkoutPage = petstore.goToCartPage().checkout();
        ReceiptPage receiptPage = checkoutPage.
                willBillTo(firstName, lastName, email).
                willPayUsingCreditCard(cardType, cardNumber, cardExpiryDate).
                confirm();
        orderNumber = receiptPage.getOrderNumber();
        receiptPage.continueShopping();
    }

    public void hasReceiptTotal(String total) {
        petstore.goToReceiptPage(orderNumber).showsTotalPaid(total);
    }

    public void containsItems(Item... items) {
        for (Item item : items) {
            petstore.goToReceiptPage(orderNumber).showsLineItem(item.number, item.description, item.totalPrice);
        }
    }

    public void wasPaidUsing(String cardType, String cardNumber, String cardExpiryDate) {
        petstore.goToReceiptPage(orderNumber).showsCreditCardDetails(cardType, cardNumber, cardExpiryDate);
    }

    public void isBilledTo(String firstName, String lastName, String emailAddress) {
        petstore.goToReceiptPage(orderNumber).showsBillingInformation(firstName, lastName, emailAddress);
    }
}
