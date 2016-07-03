package org.testinfected.petstore.views;

import org.testinfected.petstore.product.Item;

import java.util.ArrayList;
import java.util.Collection;

public class AvailableItems {

    private final Collection<Item> items = new ArrayList<>();

    public AvailableItems() {}

    public AvailableItems add(Collection<Item> available) {
        items.addAll(available);
        return this;
    }

    public Iterable<Item> getEach() {
        return items;
    }

    public boolean isNone() {
        return items.isEmpty();
    }

    public int getCount() {
        return items.size();
    }
}