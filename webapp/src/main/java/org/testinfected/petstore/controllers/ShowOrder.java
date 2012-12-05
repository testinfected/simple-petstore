package org.testinfected.petstore.controllers;

import org.testinfected.petstore.Controller;
import org.testinfected.petstore.order.Order;
import org.testinfected.petstore.order.OrderBook;
import org.testinfected.petstore.order.OrderNumber;
import org.testinfected.support.Request;
import org.testinfected.support.Response;

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
