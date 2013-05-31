package test.unit.org.testinfected.petstore.controllers;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.testinfected.molecule.HttpStatus;
import org.testinfected.petstore.controllers.CreateProduct;
import org.testinfected.petstore.procurement.ProcurementRequestHandler;
import org.testinfected.petstore.product.DuplicateProductException;
import test.support.org.testinfected.molecule.unit.MockRequest;
import test.support.org.testinfected.molecule.unit.MockResponse;

import static test.support.org.testinfected.petstore.builders.ProductBuilder.aProduct;

public class CreateProductTest {
    @Rule public JUnitRuleMockery context = new JUnitRuleMockery();

    ProcurementRequestHandler requestHandler = context.mock(ProcurementRequestHandler.class);
    CreateProduct createProduct = new CreateProduct(requestHandler);

    MockRequest request = new MockRequest();
    MockResponse response = new MockResponse();

    @Before public void
    addProductDetailsToRequest() {
        request.addParameter("number", "LAB-1234");
        request.addParameter("name", "Labrador");
        request.addParameter("description", "Friendly Dog");
        request.addParameter("photo", "labrador.jpg");
    }

    @Test public void
    makesProductProcurementRequestAndRespondsWithCreated() throws Exception {
        context.checking(new Expectations() {{
            oneOf(requestHandler).addProductToCatalog(with("LAB-1234"), with("Labrador"), with("Friendly Dog"), with("labrador.jpg"));
        }});

        createProduct.handle(request, response);
        response.assertStatus(HttpStatus.CREATED);
    }

    @Test public void
    reportsResourceConflictWhenProductAlreadyExists() throws Exception {
        context.checking(new Expectations() {{
            oneOf(requestHandler).addProductToCatalog(with(any(String.class)), with(any(String.class)), with(any(String.class)), with(any(String.class))); will(throwException(new DuplicateProductException(aProduct().build())));
        }});

        createProduct.handle(request, response);
        response.assertStatus(HttpStatus.CONFLICT);
    }
}