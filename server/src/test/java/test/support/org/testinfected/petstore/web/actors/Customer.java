package test.support.org.testinfected.petstore.web.actors;

import test.support.org.testinfected.petstore.web.ScenarioContext;
import test.support.org.testinfected.petstore.web.actors.activities.CatalogBrowsing;
import test.support.org.testinfected.petstore.web.actors.activities.Ordering;
import test.support.org.testinfected.petstore.web.drivers.ApplicationDriver;

public class Customer {

    private final ScenarioContext context;
    private final ApplicationDriver application;

    public Customer(ScenarioContext context, ApplicationDriver application) {
        this.context = context;
        this.application = application;
    }

    public void done() {
        application.logout().leave();
    }

    public Customer loginAs(String username) {
        application.enter().loginAs(username);
        return this;
    }

    public Customer seesCartIsEmpty() {
        application.displaysCartItemCount(0);
        return this;
    }

    public Customer seesCartTotalQuantity(int quantity) {
        application.displaysCartItemCount(quantity);
        return this;
    }

    public Ordering startShopping() {
        application.enter();
        return new Ordering(application, context);
    }

    public Ordering continueShopping() {
        return new Ordering(application, context);
    }

    public CatalogBrowsing browseCatalog() {
        application.enter();
        return new CatalogBrowsing(application, context);
    }
}