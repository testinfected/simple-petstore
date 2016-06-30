package test.unit.org.testinfected.petstore.controllers;

import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import com.vtence.molecule.http.HttpStatus;
import org.testinfected.petstore.controllers.CreateItem;
import org.testinfected.petstore.procurement.ProcurementRequestHandler;
import org.testinfected.petstore.product.DuplicateItemException;

import java.math.BigDecimal;

import static com.vtence.molecule.testing.ResponseAssert.assertThat;
import static test.support.org.testinfected.petstore.builders.ItemBuilder.anItem;

public class CreateItemTest {
    @Rule public JUnitRuleMockery context = new JUnitRuleMockery();

    ProcurementRequestHandler requestHandler = context.mock(ProcurementRequestHandler.class);
    CreateItem createItem = new CreateItem(requestHandler);

    Request request = new Request();
    Response response = new Response();

    @Before public void
    addItemDetailsToRequest() {
        request.addParameter("description", "Chocolate Male")
               .addParameter("product", "LAB-1234")
               .addParameter("number", "12345678")
               .addParameter("price", "599.00");
    }

    @Test public void
    makesItemProcurementRequestAndRespondsWithCreated() throws Exception {
        context.checking(new Expectations() {{
            oneOf(requestHandler).addToInventory(with("LAB-1234"), with("12345678"), with("Chocolate Male"), with(new BigDecimal("599.00")));
        }});

        createItem.handle(request, response);
        assertThat(response).hasStatus(HttpStatus.CREATED).isDone();
    }

    @Test public void
    reportsResourceConflictItemAlreadyExists() throws Exception {
        context.checking(new Expectations() {{
            oneOf(requestHandler).addToInventory(with(any(String.class)),
                    with(any(String.class)), with(any(String.class)), with(any(BigDecimal.class)));
            will(throwException(new DuplicateItemException(anItem().build())));
        }});

        createItem.handle(request, response);
        assertThat(response).hasStatus(HttpStatus.CONFLICT).isDone();
    }
}
