package test.support.org.testinfected.petstore.web.actors;

import test.support.org.testinfected.petstore.web.actors.activities.StoreManagement;
import test.support.org.testinfected.petstore.web.drivers.APIDriver;

import java.io.IOException;

public class Administrator {

    private final APIDriver api;

    public Administrator(APIDriver api) {
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