package org.testinfected.petstore.product;

import java.util.List;

public interface ItemInventory {

	List<Item> findByProductNumber(String productNumber);

    Item find(ItemNumber itemNumber);

    void add(Item item) throws DuplicateItemException;
}
