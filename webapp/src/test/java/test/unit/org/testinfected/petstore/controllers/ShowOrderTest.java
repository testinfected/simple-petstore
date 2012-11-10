package test.unit.org.testinfected.petstore.controllers;

import com.pyxis.petstore.domain.order.Order;
import com.pyxis.petstore.domain.order.OrderBook;
import com.pyxis.petstore.domain.order.OrderNumber;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.petstore.Controller;
import org.testinfected.petstore.controllers.ShowOrder;

import static org.hamcrest.Matchers.hasEntry;
import static test.support.com.pyxis.petstore.builders.OrderBuilder.anOrder;

@RunWith(JMock.class)
public class ShowOrderTest {

    Mockery context = new JUnit4Mockery();
    OrderBook orderBook = context.mock(OrderBook.class);
    ShowOrder showOrder = new ShowOrder(orderBook);

    Controller.Request request = context.mock(Controller.Request.class);
    Controller.Response response = context.mock(Controller.Response.class);

    String orderNumber = "00000100";

    @Test public void
    fetchesOrderByNumberAndDisplaysReceipt() throws Exception {
        final Order order = anOrder().build();
        context.checking(new Expectations() {{
            allowing(orderBook).find(new OrderNumber(orderNumber)); will(returnValue(order));
            oneOf(response).render(with("order"), with(hasEntry("order", order)));
        }});
        showOrder.process(request, response);
    }

    @Before public void
    stubHttpRequest() {
        context.checking(new Expectations() {{
            allowing(request).getParameter("number"); will(returnValue(orderNumber));
        }});
    }
}
