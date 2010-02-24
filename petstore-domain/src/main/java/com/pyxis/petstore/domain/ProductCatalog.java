package com.pyxis.petstore.domain;

import java.util.List;

public interface ProductCatalog {

	List<Product> findProductsByKeyword(String keyword);

}
