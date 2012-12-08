package test.unit.org.testinfected.petstore.controllers;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.petstore.controllers.CreateProduct;
import org.testinfected.petstore.procurement.ProcurementRequestHandler;
import org.testinfected.petstore.product.DuplicateProductException;
import org.testinfected.molecule.HttpStatus;
import test.support.org.testinfected.molecule.web.MockRequest;
import test.support.org.testinfected.molecule.web.MockResponse;

import static test.support.org.testinfected.petstore.builders.ProductBuilder.aProduct;
import static test.support.org.testinfected.molecule.web.MockRequest.aRequest;
import static test.support.org.testinfected.molecule.web.MockResponse.aResponse;

@RunWith(JMock.class)
public class CreateProductTest {

    Mockery context = new JUnit4Mockery();
    ProcurementRequestHandler requestHandler = context.mock(ProcurementRequestHandler.class);
    CreateProduct createProduct = new CreateProduct(requestHandler);

    MockRequest request = aRequest();
    MockResponse response = aResponse();

    @Before public void
    prepareRequest() {
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
    respondsWithConflictWhenProductAlreadyExists() throws Exception {
        context.checking(new Expectations() {{
            oneOf(requestHandler).addProductToCatalog(with(any(String.class)), with(any(String.class)), with(any(String.class)), with(any(String.class))); will(throwException(new DuplicateProductException(aProduct().build())));
        }});

        createProduct.handle(request, response);

        response.assertStatus(HttpStatus.CONFLICT);
    }
}