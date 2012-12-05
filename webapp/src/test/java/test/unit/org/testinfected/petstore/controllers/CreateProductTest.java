package test.unit.org.testinfected.petstore.controllers;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.petstore.controllers.CreateProduct;
import org.testinfected.petstore.controllers.HttpCodes;
import org.testinfected.petstore.procurement.ProcurementRequestHandler;
import org.testinfected.petstore.product.DuplicateProductException;
import org.testinfected.support.Request;
import org.testinfected.support.Response;

import static test.support.org.testinfected.petstore.builders.ProductBuilder.aProduct;

@RunWith(JMock.class)
public class CreateProductTest {

    Mockery context = new JUnit4Mockery();
    ProcurementRequestHandler requestHandler = context.mock(ProcurementRequestHandler.class);
    CreateProduct createProduct = new CreateProduct(requestHandler);

    Request request = context.mock(Request.class);
    Response response = context.mock(Response.class);

    @Before public void prepareRequest() {
        setRequestParametersTo("LAB-1234", "Labrador", "Friendly Dog", "labrador.jpg");
    }

    @Test public void
    makesProductProcurementRequestAndRespondsWithCreated() throws Exception {
        context.checking(new Expectations() {{
            oneOf(requestHandler).addProductToCatalog(with("LAB-1234"), with("Labrador"), with("Friendly Dog"), with("labrador.jpg"));
            oneOf(response).renderHead(HttpCodes.CREATED);
        }});

        createProduct.process(request, response);
    }

    @Test public void
    respondsWithConflictWhenProductAlreadyExists() throws Exception {
        context.checking(new Expectations() {{
            oneOf(requestHandler).addProductToCatalog(with(any(String.class)), with(any(String.class)), with(any(String.class)), with(any(String.class)));
                will(throwException(new DuplicateProductException(aProduct().build())));
            oneOf(response).renderHead(HttpCodes.CONFLICT);
        }});

        createProduct.process(request, response);
    }

    private void setRequestParametersTo(final String number, final String name, final String description, final String photoFileName) {
        context.checking(new Expectations() {{
            allowing(request).parameter("number"); will(returnValue(number));
            allowing(request).parameter("name"); will(returnValue(name));
            allowing(request).parameter("description"); will(returnValue(description));
            allowing(request).parameter("photo"); will(returnValue(photoFileName));
        }});
    }
}