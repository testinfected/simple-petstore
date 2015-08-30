package test.support.org.testinfected.petstore.web.actors;

import test.support.org.testinfected.petstore.web.drivers.APIDriver;
import test.support.org.testinfected.petstore.web.ScenarioContext;
import test.support.org.testinfected.petstore.web.actors.activities.StoreManagement;

import java.io.IOException;

public class Administrator {

    private final ScenarioContext context;
    private final APIDriver api;

    public Administrator(ScenarioContext context, APIDriver api) {
        this.context = context;
        this.api = api;
    }

    public void addProductToCatalog(String number, String name, String description, String photo) throws IOException {
        manageStore().addProduct(number, name, description, photo);
    }

    public void addItemToInventory(String productNumber, String itemNumber, String itemDescription, String itemPrice) throws IOException {
        manageStore().addItem(productNumber, itemNumber, itemDescription, itemPrice);
    }

    public StoreManagement manageStore() {
        return new StoreManagement(api);
    }
}