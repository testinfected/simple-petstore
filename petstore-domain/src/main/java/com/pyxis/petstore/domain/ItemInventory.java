package com.pyxis.petstore.domain;

import org.springframework.stereotype.Repository;

import java.util.List;

public @Repository interface ItemInventory {

	List<Item> findByProductNumber(String productNumber);
}
