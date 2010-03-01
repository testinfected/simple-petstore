package com.pyxis.petstore.domain;

import java.util.List;

//@Repository
public interface ProductCatalog {

	List<Product> findProductsByKeyword(String keyword);
}
