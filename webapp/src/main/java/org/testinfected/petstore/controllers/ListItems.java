package org.testinfected.petstore.controllers;

import com.vtence.molecule.Application;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import org.testinfected.petstore.View;
import org.testinfected.petstore.product.Item;
import org.testinfected.petstore.product.ItemInventory;
import org.testinfected.petstore.views.AvailableItems;

import java.util.List;

public class ListItems implements Application {

    private final ItemInventory itemInventory;
    private final View<AvailableItems> view;

    public ListItems(ItemInventory itemInventory, View<AvailableItems> view) {
        this.itemInventory = itemInventory;
        this.view = view;
    }

    public void handle(Request request, Response response) throws Exception {
        String productNumber = request.parameter("product");
        List<Item> found = itemInventory.findByProductNumber(productNumber);
        view.render(response, new AvailableItems().add(found));
    }
}