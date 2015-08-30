package test.support.org.testinfected.petstore.web.actors;

import test.support.org.testinfected.petstore.web.ScenarioContext;
import test.support.org.testinfected.petstore.web.actors.activities.CatalogBrowsing;
import test.support.org.testinfected.petstore.web.actors.activities.Ordering;
import test.support.org.testinfected.petstore.web.drivers.ApplicationDriver;

public class Customer {

    private final ScenarioContext context;
    private final ApplicationDriver application;

    private String username = "customer";

    public Customer(ScenarioContext context, ApplicationDriver application) {
        this.context = context;
        this.application = application;
    }

    public void done() {
        application.leave();
    }

    public Customer login() {
        application.enter();
        if (!application.isLoggedIn(username)) {
            application.logout();
            application.loginAs(username);
        }
        return this;
    }

    public Customer seesCartIsEmpty() {
        application.displaysCartItemCount(0);
        return this;
    }

    public Customer seesCartTotalQuantity(int quantity) {
        login();
        application.displaysCartItemCount(quantity);
        return this;
    }

    public CatalogBrowsing searchFor(String term) {
        return login().inCatalog().searchFor(term);
    }

    public CatalogBrowsing lookUpProductNamed(String productName) {
        return login().inCatalog().lookUpProductByName(productName);
    }

    public CatalogBrowsing checkInventoryOf(String productName) {
        return login().inCatalog().checkProductAvailability(productName);
    }

    public Ordering addToCart(String productName, String itemNumber) {
        return login().order().addToCart(productName, itemNumber);
    }

    public Ordering pay(String firstName, String lastName, String email, String cardType, String cardNumber, String cardExpiryDate) {
        return login().order().confirm(firstName, lastName, email, cardType, cardNumber, cardExpiryDate);
    }

    private Ordering order() {
        return new Ordering(application, context);
    }

    private CatalogBrowsing inCatalog() {
        return new CatalogBrowsing(application, context);
    }
}