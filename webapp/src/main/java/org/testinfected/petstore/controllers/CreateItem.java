package org.testinfected.petstore.controllers;

import org.testinfected.petstore.Controller;
import org.testinfected.petstore.procurement.ProcurementRequestHandler;
import org.testinfected.petstore.product.DuplicateItemException;
import org.testinfected.support.Request;
import org.testinfected.support.Response;

import java.math.BigDecimal;

public class CreateItem implements Controller {

    private final ProcurementRequestHandler requestHandler;

    public CreateItem(ProcurementRequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    public void process(Request request, Response response) throws Exception {
        try {
            requestHandler.addToInventory(
                    request.getParameter("product"),
                    request.getParameter("number"),
                    request.getParameter("description"),
                    new BigDecimal(request.getParameter("price")));
            response.renderHead(HttpCodes.CREATED);
        } catch (DuplicateItemException e) {
            response.renderHead(HttpCodes.CONFLICT);
        }
    }
}
