package org.testinfected.petstore.procurement;

import com.pyxis.petstore.domain.product.Product;

public interface ProcurementRequestListener {

    void addProduct(Product product) throws Exception;
}
