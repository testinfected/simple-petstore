package org.testinfected.petstore.product;

import java.util.List;

public interface ProductCatalog {

    void add(Product product) throws DuplicateProductException;

    Product findByNumber(String productNumber);

	List<Product> findByKeyword(String keyword);
}
