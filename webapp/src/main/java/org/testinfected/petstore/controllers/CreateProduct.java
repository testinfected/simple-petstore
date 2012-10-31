package org.testinfected.petstore.controllers;

import org.testinfected.petstore.Controller;
import org.testinfected.petstore.procurement.ProcurementRequestHandler;

public class CreateProduct implements Controller {

    public static final int CREATED = 201;

    private final ProcurementRequestHandler requestHandler;

    public CreateProduct(ProcurementRequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    public void process(Request request, Response response) throws Exception {
        requestHandler.addProductToCatalog(
                request.getParameter("number"),
                request.getParameter("name"),
                request.getParameter("description"),
                request.getParameter("photo"));

        response.renderHead(CREATED);
    }
}