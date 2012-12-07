package test.unit.org.testinfected.petstore.controllers;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.petstore.Page;
import org.testinfected.petstore.controllers.ShowOrder;
import org.testinfected.petstore.order.Order;
import org.testinfected.petstore.order.OrderBook;
import org.testinfected.petstore.order.OrderNumber;
import test.support.org.testinfected.support.web.MockRequest;
import test.support.org.testinfected.support.web.MockResponse;

import static org.hamcrest.Matchers.hasEntry;
import static test.support.org.testinfected.petstore.builders.OrderBuilder.anOrder;

@RunWith(JMock.class)
public class ShowOrderTest {

    Mockery context = new JUnit4Mockery();
    OrderBook orderBook = context.mock(OrderBook.class);
    Page orderPage = context.mock(Page.class);
    ShowOrder showOrder = new ShowOrder(orderBook, orderPage);

    MockRequest request = MockRequest.aRequest();
    MockResponse response = MockResponse.aResponse();

    String orderNumber = "00000100";

    @Test public void
    fetchesOrderByNumberAndDisplaysReceipt() throws Exception {
        final Order order = anOrder().build();
        request.addParameter("number", orderNumber);

        context.checking(new Expectations() {{
            allowing(orderBook).find(new OrderNumber(orderNumber)); will(returnValue(order));
            oneOf(orderPage).render(with(response), with(hasEntry("order", order)));
        }});

        showOrder.handle(request, response);
    }
}
