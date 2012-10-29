package org.testinfected.petstore.controllers;

import com.pyxis.petstore.domain.product.ProductCatalog;
import org.testinfected.petstore.Controller;
import org.testinfected.petstore.procurement.ProcurementRequestListener;

public class CreateItem implements Controller {

    public CreateItem(ProductCatalog productCatalog, ProcurementRequestListener requestListener) {
    }

    public void process(Request request, Response response) throws Exception {
    }
}
