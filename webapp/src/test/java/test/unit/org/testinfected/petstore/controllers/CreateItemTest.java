package test.unit.org.testinfected.petstore.controllers;

import com.pyxis.petstore.domain.product.ProductCatalog;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.petstore.Controller;
import org.testinfected.petstore.controllers.CreateItem;
import org.testinfected.petstore.procurement.ProcurementRequestHandler;

import java.math.BigDecimal;

@RunWith(JMock.class)
public class CreateItemTest {

    Mockery context = new JUnit4Mockery();
    ProcurementRequestHandler requestHandler = context.mock(ProcurementRequestHandler.class);
    ProductCatalog productCatalog = context.mock(ProductCatalog.class);
    CreateItem createItem = new CreateItem(requestHandler);

    Controller.Request request = context.mock(Controller.Request.class);
    Controller.Response response = context.mock(Controller.Response.class);
    final int CREATED = 201;

    @Test public void
    makesItemProcurementRequestAndRespondsWithCreated() throws Exception {
        setRequestParametersTo("LAB-1234", "12345678", "Chocolate Male", "599.00");

        context.checking(new Expectations() {{
            oneOf(requestHandler).addToInventory(with("LAB-1234"), with("12345678"), with("Chocolate Male"), with(new BigDecimal("599.00")));
        }});

        context.checking(new Expectations() {{
            oneOf(response).renderHead(CREATED);
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
