package org.testinfected.petstore.procurement;

import com.pyxis.petstore.domain.product.Item;
import com.pyxis.petstore.domain.product.Product;

public interface ProcurementRequestListener {

    void addProduct(Product product) throws Exception;

    void addItem(Item item);
}
