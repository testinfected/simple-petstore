package test.unit.org.testinfected.petstore.controllers;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.petstore.controllers.CreateItem;
import org.testinfected.petstore.procurement.ProcurementRequestHandler;
import org.testinfected.petstore.product.DuplicateItemException;
import org.testinfected.support.HttpStatus;
import test.support.org.testinfected.support.web.MockRequest;
import test.support.org.testinfected.support.web.MockResponse;

import java.math.BigDecimal;

import static test.support.org.testinfected.petstore.builders.ItemBuilder.anItem;
import static test.support.org.testinfected.support.web.MockRequest.aRequest;
import static test.support.org.testinfected.support.web.MockResponse.aResponse;

@RunWith(JMock.class)
public class CreateItemTest {

    Mockery context = new JUnit4Mockery();
    ProcurementRequestHandler requestHandler = context.mock(ProcurementRequestHandler.class);
    CreateItem createItem = new CreateItem(requestHandler);

    MockRequest request = aRequest();
    MockResponse response = aResponse();

    @Before public void
    prepareRequest() {
        request.addParameter("product", "LAB-1234");
        request.addParameter("number", "12345678");
        request.addParameter("description", "Chocolate Male");
        request.addParameter("price", "599.00");
    }

    @Test public void
    makesItemProcurementRequestAndRespondsWithCreated() throws Exception {
        context.checking(new Expectations() {{
            oneOf(requestHandler).addToInventory(with("LAB-1234"), with("12345678"), with("Chocolate Male"), with(new BigDecimal("599.00")));
        }});

        createItem.handle(request, response);

        response.assertStatus(HttpStatus.CREATED);
    }

    @Test public void
    respondsWithForbiddenWhenItemAlreadyExists() throws Exception {
        context.checking(new Expectations() {{
            oneOf(requestHandler).addToInventory(with(any(String.class)), with(any(String.class)), with(any(String.class)), with(any(BigDecimal.class))); will(throwException(new DuplicateItemException(anItem().build())));
        }});

        createItem.handle(request, response);

        response.assertStatus(HttpStatus.CONFLICT);
    }
}
