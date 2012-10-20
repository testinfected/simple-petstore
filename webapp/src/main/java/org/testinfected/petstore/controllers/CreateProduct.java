package org.testinfected.petstore.controllers;

import com.pyxis.petstore.domain.product.Product;
import org.testinfected.petstore.Controller;
import org.testinfected.petstore.procurement.ProcurementRequestListener;

public class CreateProduct implements Controller {

    public static final int CREATED = 201;

    private final ProcurementRequestListener requestListener;

    public CreateProduct(ProcurementRequestListener requestListener) {
        this.requestListener = requestListener;
    }

    public void process(Request request, Response response) throws Exception {
        final Product product = new Product(request.getParameter("number"), request.getParameter("name"));
        product.setDescription(request.getParameter("description"));
        requestListener.addProduct(product);

        response.renderHead(CREATED);
    }
}
