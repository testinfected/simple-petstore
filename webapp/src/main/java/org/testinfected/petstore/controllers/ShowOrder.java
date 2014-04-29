package org.testinfected.petstore.controllers;

import com.vtence.molecule.Application;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import org.testinfected.petstore.View;
import org.testinfected.petstore.order.Order;
import org.testinfected.petstore.order.OrderBook;
import org.testinfected.petstore.order.OrderNumber;

public class ShowOrder implements Application {
    private final OrderBook orderBook;
    private final View<Order> view;

    public ShowOrder(OrderBook orderBook, View<Order> view) {
        this.orderBook = orderBook;
        this.view = view;
    }

    public void handle(Request request, Response response) throws Exception {
        String number = request.parameter("number");
        Order order = orderBook.find(new OrderNumber(number));
        view.render(response, order);
    }
}