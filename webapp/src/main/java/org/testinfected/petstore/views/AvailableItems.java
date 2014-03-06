package org.testinfected.petstore.views;

import org.testinfected.petstore.product.Item;

import java.util.ArrayList;
import java.util.Collection;

public class AvailableItems {

    private final Collection<Item> items = new ArrayList<Item>();

    public AvailableItems() {}

    public AvailableItems add(Collection<Item> available) {
        items.addAll(available);
        return this;
    }

    public Iterable<Item> getEach() {
        return items;
    }

    // JMustache does not understand standard bean properties
    // todo submit a pull request
    public boolean getNone() {
        return items.isEmpty();
    }

    public int getCount() {
        return items.size();
    }
}