package org.testinfected.petstore.controllers;

import com.vtence.molecule.Application;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import org.testinfected.petstore.Page;
import org.testinfected.petstore.product.Item;
import org.testinfected.petstore.product.ItemInventory;
import org.testinfected.petstore.views.AvailableItems;

import java.util.List;

public class ListItems implements Application {

    private final ItemInventory itemInventory;
    private final Page itemsPage;

    public ListItems(ItemInventory itemInventory, Page itemsPage) {
        this.itemInventory = itemInventory;
        this.itemsPage = itemsPage;
    }

    public void handle(Request request, Response response) throws Exception {
        String productNumber = request.parameter("product");
        List<Item> found = itemInventory.findByProductNumber(productNumber);
        itemsPage.render(response, new AvailableItems().add(found));
    }
}
