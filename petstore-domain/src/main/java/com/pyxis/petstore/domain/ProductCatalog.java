package com.pyxis.petstore.domain;

import org.springframework.stereotype.Repository;

import java.util.List;

public @Repository interface ProductCatalog {

	List<Product> findByKeyword(String keyword);

	void add(Product product);
}
