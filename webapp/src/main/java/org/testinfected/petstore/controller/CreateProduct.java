package org.testinfected.petstore.controller;

import org.testinfected.petstore.procurement.ProcurementRequestListener;
import com.pyxis.petstore.domain.product.Product;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.Status;
import org.testinfected.petstore.Application;

public class CreateProduct implements Application {

    private final ProcurementRequestListener requestListener;

    public CreateProduct(ProcurementRequestListener requestListener) {
        this.requestListener = requestListener;
    }

    public void handle(Request request, Response response) throws Exception {
        addProductToCatalog(request);
        renderCreated(response);
    }

    private void addProductToCatalog(Request request) throws Exception {
        final Product product = new Product(request.getParameter("number"), request.getParameter("name"));
        product.setDescription(request.getParameter("description"));
        requestListener.addProduct(product);
    }

    private void renderCreated(Response response) {
        response.setCode(Status.CREATED.getCode());
        response.setText(Status.CREATED.getDescription());
    }
}
