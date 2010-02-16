package com.pyxis.petstore.domain;

import java.util.List;

public interface ItemRepository {

	List<Item> findItemsByKeyword(String query);

}
