package org.testinfected.petstore.views;

import org.testinfected.petstore.product.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class AvailableItems {

    private final Collection<Item> items = new ArrayList<Item>();

    public AvailableItems() {
        this(Collections.<Item>emptyList());
    }

    public AvailableItems(Collection<Item> available) {
        addAll(available);
    }

    public void addAll(Collection<Item> available) {
        items.addAll(available);
    }

    public Iterable<Item> getEach() {
        return items;
    }

    public boolean getNone() {
        return items.isEmpty();
    }

    public int getCount() {
        return items.size();
    }

    public String toString() {
        return items.toString();
    }
}