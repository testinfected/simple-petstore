package org.testinfected.petstore.controllers;

import org.testinfected.petstore.Controller;
import org.testinfected.petstore.procurement.ProcurementRequestHandler;

import java.math.BigDecimal;

public class CreateItem implements Controller {

    private static final int CREATED = 201;

    private final ProcurementRequestHandler requestHandler;

    public CreateItem(ProcurementRequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    public void process(Request request, Response response) throws Exception {
        requestHandler.addToInventory(
                request.getParameter("product"),
                request.getParameter("number"),
                request.getParameter("description"),
                new BigDecimal(request.getParameter("price")));

        response.renderHead(CREATED);
    }
}
