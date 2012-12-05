package test.unit.org.testinfected.petstore.controllers;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.petstore.controllers.CreateItem;
import org.testinfected.petstore.controllers.HttpCodes;
import org.testinfected.petstore.procurement.ProcurementRequestHandler;
import org.testinfected.petstore.product.DuplicateItemException;
import org.testinfected.support.Request;
import org.testinfected.support.Response;

import java.math.BigDecimal;

import static test.support.org.testinfected.petstore.builders.ItemBuilder.anItem;

@RunWith(JMock.class)
public class CreateItemTest {

    Mockery context = new JUnit4Mockery();
    ProcurementRequestHandler requestHandler = context.mock(ProcurementRequestHandler.class);
    CreateItem createItem = new CreateItem(requestHandler);

    Request request = context.mock(Request.class);
    Response response = context.mock(Response.class);

    @Before public void prepareRequest() {
        setRequestParametersTo("LAB-1234", "12345678", "Chocolate Male", "599.00");
    }

    @Test public void
    makesItemProcurementRequestAndRespondsWithCreated() throws Exception {
        context.checking(new Expectations() {{
            oneOf(requestHandler).addToInventory(with("LAB-1234"), with("12345678"), with("Chocolate Male"), with(new BigDecimal("599.00")));
            oneOf(response).renderHead(HttpCodes.CREATED);
        }});

        createItem.process(request, response);
    }

    @Test public void
    respondsWithForbiddenWhenItemAlreadyExists() throws Exception {
        context.checking(new Expectations() {{
            oneOf(requestHandler).addToInventory(with(any(String.class)), with(any(String.class)), with(any(String.class)), with(any(BigDecimal.class)));
                will(throwException(new DuplicateItemException(anItem().build())));
            oneOf(response).renderHead(HttpCodes.CONFLICT);
        }});

        createItem.process(request, response);
    }

    private void setRequestParametersTo(final String productNumber, final String itemNumber, final String description, final String price) {
        context.checking(new Expectations() {{
            allowing(request).getParameter("product"); will(returnValue(productNumber));
            allowing(request).getParameter("number"); will(returnValue(itemNumber));
            allowing(request).getParameter("description"); will(returnValue(description));
            allowing(request).getParameter("price"); will(returnValue(price));
        }});
    }
}
