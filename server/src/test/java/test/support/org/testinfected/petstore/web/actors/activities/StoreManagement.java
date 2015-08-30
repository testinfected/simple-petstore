package test.support.org.testinfected.petstore.web.actors.activities;

import test.support.org.testinfected.petstore.web.drivers.APIDriver;

import java.io.IOException;

public class StoreManagement {

    private final APIDriver api;

    public StoreManagement(APIDriver api) {
        this.api = api;
    }

    public void addProduct(String number, String name, String description, String photo) throws IOException {
        api.addProduct(number, name, description, photo);
    }

    public void addItem(String productNumber, String itemNumber, String itemDescription, String itemPrice) throws IOException {
        api.addItem(productNumber, itemNumber, itemDescription, itemPrice);
    }
}