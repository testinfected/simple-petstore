package test.support.org.testinfected.petstore.web.activities;

import test.support.org.testinfected.petstore.web.page.PetStoreDriver;

import java.io.IOException;

public class StoreManagement {

    private final PetStoreDriver petstore;

    public StoreManagement(PetStoreDriver petstore) {
        this.petstore = petstore;
    }

    public void addProduct(String number, String name, String description, String photo) throws IOException {
        petstore.administrate().addProduct(number, name, description, photo);
    }

    public void addItem(String productNumber, String itemNumber, String itemDescription, String itemPrice) throws IOException {
        petstore.administrate().addItem(productNumber, itemNumber, itemDescription, itemPrice);
    }
}
