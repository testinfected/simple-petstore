package test.unit.org.testinfected.petstore.controllers;

import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import org.testinfected.petstore.controllers.ShowOrder;
import org.testinfected.petstore.order.Order;
import org.testinfected.petstore.order.OrderBook;
import org.testinfected.petstore.order.OrderNumber;
import test.support.org.testinfected.petstore.web.MockView;

import static test.support.org.testinfected.petstore.builders.OrderBuilder.anOrder;

public class ShowOrderTest {
    @Rule public JUnitRuleMockery context = new JUnitRuleMockery();

    OrderBook orderBook = context.mock(OrderBook.class);
    MockView<Order> view = new MockView<>();
    ShowOrder showOrder = new ShowOrder(orderBook, view);

    Request request = new Request();
    Response response = new Response();

    String orderNumber = "00000100";

    @Test public void
    fetchesOrderByNumberAndRendersReceipt() throws Exception {
        final Order order = anOrder().withNumber(orderNumber).build();
        request.addParameter("number", orderNumber);

        orderBookContains(order);

        showOrder.handle(request, response);
        view.assertRenderedTo(response);
        view.assertRenderedWith(sameOrderAs(order));
    }

    private Matcher<Object> sameOrderAs(Order order) {
        return Matchers.sameInstance(order);
    }

    private void orderBookContains(final Order order) {
        context.checking(new Expectations() {{
            allowing(orderBook).find(new OrderNumber(order.getNumber())); will(returnValue(order));
        }});
    }
}