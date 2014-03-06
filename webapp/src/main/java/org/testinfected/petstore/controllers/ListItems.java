package org.testinfected.petstore.controllers;

import org.testinfected.molecule.Application;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;
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
        List<Item> items = itemInventory.findByProductNumber(productNumber);
        itemsPage.render(response, new AvailableItems(items));
    }
}
