package test.support.org.testinfected.petstore.web.actors.activities;

import test.support.org.testinfected.petstore.web.ScenarioContext;
import test.support.org.testinfected.petstore.web.drivers.ApplicationDriver;
import test.support.org.testinfected.petstore.web.drivers.pages.ItemsPage;
import test.support.org.testinfected.petstore.web.drivers.pages.ProductsPage;

public class CatalogBrowsing {

    private final ScenarioContext context;
    private final ApplicationDriver application;

    public CatalogBrowsing(ApplicationDriver application, ScenarioContext context) {
        this.context = context;
        this.application = application;
    }

    public CatalogBrowsing searchFor(String term) {
        context.set(application.search(term));
        return this;
    }

    public void obtainsNoResult() {
        context.get(ProductsPage.class).showsNoResult();
    }

    public CatalogBrowsing lookUpProductByName(String name) {
        context.set("productName", name);
        searchFor(name);
        return this;
    }

    public CatalogBrowsing seesAvailableProduct(String productNumber) {
        String productName = context.get("productName");
        context.get(ProductsPage.class).displaysProduct(productNumber, productName);
        return this;
    }

    public CatalogBrowsing checkAvailabilityOfProduct(String productName) {
        context.set(application.search(productName).selectProduct(productName));
        return this;
    }

    public void seesNoItemAvailable() {
        context.get(ItemsPage.class).showsNoItemAvailable();
    }

    public CatalogBrowsing seesAvailableItem(String itemNumber, String itemDescription, String itemPrice) {
        context.get(ItemsPage.class).displaysItem(itemNumber, itemDescription, itemPrice);
        return this;
    }
}