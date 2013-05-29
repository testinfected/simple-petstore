package org.testinfected.petstore.controllers;

import org.testinfected.molecule.Application;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;
import org.testinfected.petstore.billing.Address;
import org.testinfected.petstore.billing.CreditCardDetails;
import org.testinfected.petstore.order.Cart;
import org.testinfected.petstore.order.OrderNumber;
import org.testinfected.petstore.order.SalesAssistant;
import org.testinfected.petstore.util.SessionScope;

import static org.testinfected.petstore.billing.CreditCardType.valueOf;

public class PlaceOrder implements Application {
    private final SalesAssistant salesAssistant;

    public PlaceOrder(SalesAssistant salesAssistant) {
        this.salesAssistant = salesAssistant;
    }

    public void handle(Request request, Response response) throws Exception {
        OrderNumber orderNumber = salesAssistant.placeOrder(cartFor(request), readPaymentDetailsFrom(request));
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

    private Cart cartFor(Request client) {
        return new SessionScope(client.session()).cart();
    }
}
