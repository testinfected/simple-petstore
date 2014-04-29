package org.testinfected.petstore.controllers;

import com.vtence.molecule.Application;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import org.testinfected.petstore.Page;
import org.testinfected.petstore.order.Order;
import org.testinfected.petstore.order.OrderBook;
import org.testinfected.petstore.order.OrderNumber;

public class ShowOrder implements Application {
    private final OrderBook orderBook;
    private final Page orderPage;

    public ShowOrder(OrderBook orderBook, Page orderPage) {
        this.orderBook = orderBook;
        this.orderPage = orderPage;
    }

    public void handle(Request request, Response response) throws Exception {
        String number = request.parameter("number");
        Order order = orderBook.find(new OrderNumber(number));
        orderPage.render(response, order);
    }
}
