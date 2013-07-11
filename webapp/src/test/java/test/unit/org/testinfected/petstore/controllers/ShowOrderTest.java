package test.unit.org.testinfected.petstore.controllers;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import org.testinfected.petstore.controllers.ShowOrder;
import org.testinfected.petstore.order.Order;
import org.testinfected.petstore.order.OrderBook;
import org.testinfected.petstore.order.OrderNumber;
import test.support.org.testinfected.molecule.unit.MockRequest;
import test.support.org.testinfected.molecule.unit.MockResponse;
import test.support.org.testinfected.petstore.web.MockPage;

import static test.support.org.testinfected.petstore.builders.OrderBuilder.anOrder;

public class ShowOrderTest {
    @Rule public JUnitRuleMockery context = new JUnitRuleMockery();

    OrderBook orderBook = context.mock(OrderBook.class);
    MockPage orderPage = new MockPage();
    ShowOrder showOrder = new ShowOrder(orderBook, orderPage);

    MockRequest request = new MockRequest();
    MockResponse response = new MockResponse();

    String orderNumber = "00000100";

    @Test public void
    fetchesOrderByNumberAndDisplaysReceipt() throws Exception {
        final Order order = anOrder().withNumber(orderNumber).build();
        request.addParameter("number", orderNumber);

        orderBookContains(order);

        showOrder.handle(request, response);
        orderPage.assertRenderedTo(response);
        orderPage.assertRenderedWith("order", order);
    }

    private void orderBookContains(final Order order) {
        context.checking(new Expectations() {{
            allowing(orderBook).find(new OrderNumber(order.getNumber())); will(returnValue(order));
        }});
    }
}
