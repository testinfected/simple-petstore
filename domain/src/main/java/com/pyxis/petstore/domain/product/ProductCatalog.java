package com.pyxis.petstore.domain.product;

import java.util.List;

public interface ProductCatalog {

    void add(Product product);

    Product findByNumber(String productNumber);

	List<Product> findByKeyword(String keyword);
}
