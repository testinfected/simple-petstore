package test.support.org.testinfected.petstore.web;

import test.support.org.testinfected.petstore.web.activities.User;
import test.support.org.testinfected.petstore.web.page.PetStore;
import test.system.org.testinfected.petstore.features.Item;
import test.system.org.testinfected.petstore.features.Product;

import java.io.IOException;

public class ApplicationDriver {

    private final PetStore petstore;

    private String admin = "admin";
    private String customer = "customer";

    private User user;

    public ApplicationDriver(TestEnvironment environment) {
        this(PetStore.in(environment));
    }

    public ApplicationDriver(PetStore petstore) {
        this.petstore = petstore;
        this.user = new User(petstore);
    }

    public void start() throws Exception {
        petstore.start();
    }

    public void stop() throws Exception {
        petstore.stop();
    }

    public void addProductToCatalog(String number, String name, String description, String photo) throws IOException {
        user.loginAs(admin).manageStore().addProduct(number, name, description, photo);
    }

    public void showsProductInCatalog(String number, String name) {
        user.loginAs(customer).catalog().containsProduct(number, name);
    }

    public void showsNoItemAvailableFor(String product) {
        user.loginAs(customer).forProduct(product).noItemIsAvailable();
    }

    public void addItemToInventory(String productNumber, String itemNumber, String itemDescription, String itemPrice) throws IOException {
        user.loginAs(admin).manageStore().addItem(productNumber, itemNumber, itemDescription, itemPrice);
    }

    public void showsAvailableItem(String product, String number, String description, String price) {
        user.loginAs(customer).forProduct(product).itemIsInStock(number, description, price);
    }

    public void havingProductInCatalog(String number, String name, String description, String photo) throws IOException {
        addProductToCatalog(number, name, description, photo);
    }

    public void havingItemInStore(String productNumber, String itemNumber, String itemDescription, String itemPrice) throws IOException {
        addItemToInventory(productNumber, itemNumber, itemDescription, itemPrice);
    }

    public void searchShowsNoResult(String term) {
        user.loginAs(customer).catalog().containsNoProductNamed(term);
    }

    public void searchDisplaysResults(String term, Product... products) {
        user.loginAs(customer).catalog().matchesProducts(term, products);
    }

    public void showsCartIsEmpty() {
        user.loginAs(customer).cartIsEmpty();
    }

    public void buyItem(String product, String itemNumber) {
        user.loginAs(customer).forProduct(product).addItemToCart(itemNumber);
    }

    public void showsCartTotalQuantity(int quantity) {
        user.loginAs(customer).cartItemCountIs(quantity);
    }

    public void showsCartContent(String totalPrice, Item... items) {
        user.loginAs(customer).cart().hasContent(totalPrice, items);
    }

    public void showsTotalToPay(String amount) {
        user.loginAs(customer).order().amountsTo(amount);
    }

    public void pay(String firstName, String lastName, String email, String cardType, String cardNumber, String cardExpiryDate) {
        user.loginAs(customer).order().confirm(firstName, lastName, email, cardType, cardNumber, cardExpiryDate);
    }

    public void showsOrderTotal(String total) {
        user.loginAs(customer).order().hasReceiptTotal(total);
    }

    public void showsOrderedItems(Item... items) {
        user.loginAs(customer).order().containsItems(items);
    }

    public void showsCreditCardDetails(String cardType, String cardNumber, String cardExpiryDate) {
        user.loginAs(customer).order().wasPaidUsing(cardType, cardNumber, cardExpiryDate);
    }

    public void showsBillingInformation(String firstName, String lastName, String emailAddress) {
        user.loginAs(customer).order().isBilledTo(firstName, lastName, emailAddress);
    }
}
