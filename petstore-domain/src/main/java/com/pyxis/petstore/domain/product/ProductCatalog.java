package com.pyxis.petstore.domain.product;

import java.util.List;

public interface ProductCatalog {

	List<Product> findByKeyword(String keyword);

	void store(Product product);
}
