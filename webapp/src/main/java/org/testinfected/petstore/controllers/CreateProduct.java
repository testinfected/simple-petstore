package org.testinfected.petstore.controllers;

import org.testinfected.petstore.procurement.ProcurementRequestHandler;
import org.testinfected.petstore.product.DuplicateProductException;
import org.testinfected.molecule.Application;
import org.testinfected.molecule.HttpStatus;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;

public class CreateProduct implements Application {

    private final ProcurementRequestHandler requestHandler;

    public CreateProduct(ProcurementRequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    public void handle(Request request, Response response) throws Exception {
        try {
            requestHandler.addProductToCatalog(
                    request.parameter("number"),
                    request.parameter("name"),
                    request.parameter("description"),
                    request.parameter("photo"));
            response.status(HttpStatus.CREATED);
        } catch (DuplicateProductException e) {
            response.status(HttpStatus.CONFLICT);
        }
    }
}