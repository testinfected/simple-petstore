package org.testinfected.petstore.jdbc;

import com.pyxis.petstore.domain.product.Item;
import com.pyxis.petstore.domain.product.ItemInventory;
import com.pyxis.petstore.domain.product.ItemNumber;

import java.util.ArrayList;
import java.util.List;

public class ItemDatabase implements ItemInventory {

    public List<Item> findByProductNumber(String productNumber) {
        return new ArrayList<Item>();
    }

    public Item find(ItemNumber itemNumber) {
        return null;
    }

    public void add(Item item) {
    }
}
