package com.pyxis.petstore.domain;

import java.util.List;

public interface ItemInventory {

	List<Item> findByProductNumber(String productNumber);

    Item find(ItemNumber itemNumber);
}
