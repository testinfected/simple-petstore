package org.testinfected.petstore.controllers;

import org.testinfected.petstore.Controller;
import org.testinfected.petstore.procurement.ProcurementRequestHandler;
import org.testinfected.petstore.product.DuplicateProductException;

public class CreateProduct implements Controller {

    private static final int CREATED = 201;
    private static final int FORBIDDEN = 409;

    private final ProcurementRequestHandler requestHandler;

    public CreateProduct(ProcurementRequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    public void process(Request request, Response response) throws Exception {
        try {
            requestHandler.addProductToCatalog(
                    request.getParameter("number"),
                    request.getParameter("name"),
                    request.getParameter("description"),
                    request.getParameter("photo"));
            response.renderHead(CREATED);
        } catch (DuplicateProductException e) {
            response.renderHead(FORBIDDEN);
        }
    }
}