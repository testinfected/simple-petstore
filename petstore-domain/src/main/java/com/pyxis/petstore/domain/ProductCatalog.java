package com.pyxis.petstore.domain;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCatalog {

	List<Product> findProductsByKeyword(String keyword);

	void add(Product product);
}
