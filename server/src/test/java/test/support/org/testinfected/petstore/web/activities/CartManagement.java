package test.support.org.testinfected.petstore.web.activities;

import test.support.org.testinfected.petstore.web.page.PetStoreDriver;
import test.system.org.testinfected.petstore.features.Item;

public class CartManagement {
    private final PetStoreDriver petstore;

    public CartManagement(PetStoreDriver petstore) {
        this.petstore = petstore;
    }

    public void hasContent(String totalPrice, Item... items) {
        for (Item item : items) {
            petstore.goToCartPage().showsGrandTotal(totalPrice).
                    showsItem(item.number, item.description, item.price, item.quantity, item.totalPrice);
        }
    }
}
