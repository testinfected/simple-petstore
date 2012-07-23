package org.testinfected.petstore.jdbc;

import com.pyxis.petstore.domain.product.Product;
import com.pyxis.petstore.domain.product.ProductCatalog;

import java.util.ArrayList;
import java.util.List;

public class ProductsDatabase implements ProductCatalog {
    public List<Product> findByKeyword(String keyword) {
        return new ArrayList<Product>();
    }

    public void add(Product product) {
    }
}
