package com.pyxis.petstore.domain;

import java.util.List;

public interface ItemCatalog {

	List<Item> findItemsByKeyword(String query);

}
