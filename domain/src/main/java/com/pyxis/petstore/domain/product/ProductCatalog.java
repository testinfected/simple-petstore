package com.pyxis.petstore.domain.product;

import java.util.List;

public interface ProductCatalog {

	List<Product> findByKeyword(String keyword);

    Product findByNumber(String productNumber);

	void add(Product product);
}
