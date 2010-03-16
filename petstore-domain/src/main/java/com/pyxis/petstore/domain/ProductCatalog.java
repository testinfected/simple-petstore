package com.pyxis.petstore.domain;

import java.util.List;

public interface ProductCatalog {

	List<Product> findByKeyword(String keyword);

	void add(Product product);
}
