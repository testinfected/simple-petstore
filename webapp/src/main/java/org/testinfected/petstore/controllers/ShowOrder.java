package org.testinfected.petstore.controllers;

import com.pyxis.petstore.domain.order.Order;
import com.pyxis.petstore.domain.order.OrderBook;
import com.pyxis.petstore.domain.order.OrderNumber;
import org.testinfected.petstore.Controller;

import static org.testinfected.petstore.util.Context.context;

public class ShowOrder implements Controller {
    private final OrderBook orderBook;

    public ShowOrder(OrderBook orderBook) {
        this.orderBook = orderBook;
    }

    public void process(Request request, Response response) throws Exception {
        String number = request.getParameter("number");
        Order order = orderBook.find(new OrderNumber(number));
        response.render("order", context().with("order", order).asMap());
    }
}
