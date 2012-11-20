package org.testinfected.petstore.controllers;

import org.testinfected.petstore.billing.Address;
import org.testinfected.petstore.billing.CreditCardDetails;
import org.testinfected.petstore.order.OrderNumber;
import org.testinfected.petstore.order.SalesAssistant;
import org.testinfected.petstore.Controller;

import static org.testinfected.petstore.billing.CreditCardType.valueOf;

public class PlaceOrder implements Controller {
    private final SalesAssistant salesAssistant;

    public PlaceOrder(SalesAssistant salesAssistant) {
        this.salesAssistant = salesAssistant;
    }

    public void process(Request request, Response response) throws Exception {
        OrderNumber orderNumber = salesAssistant.placeOrder(readPaymentDetailsFrom(request));
        response.redirectTo("/orders/" + orderNumber.getNumber());
    }

    private CreditCardDetails readPaymentDetailsFrom(Request request) {
        return new CreditCardDetails(
                    valueOf(request.getParameter("card-type")),
                    request.getParameter("card-number"),
                    request.getParameter("expiry-date"),
                    new Address(
                            request.getParameter("first-name"),
                            request.getParameter("last-name"),
                            request.getParameter("email")));
    }
}
