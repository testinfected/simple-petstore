package org.testinfected.petstore.controllers;

import org.testinfected.petstore.Controller;
import org.testinfected.petstore.product.Item;
import org.testinfected.petstore.product.ItemInventory;
import org.testinfected.support.Request;
import org.testinfected.support.Response;

import java.util.List;

import static org.testinfected.petstore.util.Context.context;

public class ListItems implements Controller {

    private final ItemInventory itemInventory;

    public ListItems(ItemInventory itemInventory) {
        this.itemInventory = itemInventory;
    }

    public void process(Request request, Response response) throws Exception {
        String productNumber = request.parameter("product");
        List<Item> items = itemInventory.findByProductNumber (productNumber);
        response.render("items", context().
                with("in-stock", !items.isEmpty()).
                and("item-count", items.size()).
                and("items", items).asMap());
    }
}
