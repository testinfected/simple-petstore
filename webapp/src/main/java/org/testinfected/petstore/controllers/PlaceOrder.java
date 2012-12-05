package org.testinfected.petstore.controllers;

import org.testinfected.petstore.Controller;
import org.testinfected.petstore.billing.Address;
import org.testinfected.petstore.billing.CreditCardDetails;
import org.testinfected.petstore.order.OrderNumber;
import org.testinfected.petstore.order.SalesAssistant;
import org.testinfected.support.Request;
import org.testinfected.support.Response;

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
                    valueOf(request.parameter("card-type")),
                    request.parameter("card-number"),
                    request.parameter("expiry-date"),
                    new Address(
                            request.parameter("first-name"),
                            request.parameter("last-name"),
                            request.parameter("email")));
    }
}
