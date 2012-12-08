package org.testinfected.petstore.controllers;

import org.testinfected.petstore.procurement.ProcurementRequestHandler;
import org.testinfected.petstore.product.DuplicateItemException;
import org.testinfected.molecule.Application;
import org.testinfected.molecule.HttpStatus;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;

import java.math.BigDecimal;

public class CreateItem implements Application {

    private final ProcurementRequestHandler requestHandler;

    public CreateItem(ProcurementRequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    public void handle(Request request, Response response) throws Exception {
        try {
            requestHandler.addToInventory(
                    request.parameter("product"),
                    request.parameter("number"),
                    request.parameter("description"),
                    new BigDecimal(request.parameter("price")));
            response.status(HttpStatus.CREATED);
        } catch (DuplicateItemException e) {
            response.status(HttpStatus.CONFLICT);
        }
    }
}
