package org.testinfected.petstore.controllers;

import com.pyxis.petstore.domain.billing.Address;
import com.pyxis.petstore.domain.billing.CreditCardDetails;
import com.pyxis.petstore.domain.order.OrderNumber;
import com.pyxis.petstore.domain.order.SalesAssistant;
import org.testinfected.petstore.Controller;

import static com.pyxis.petstore.domain.billing.CreditCardType.valueOf;

public class PlaceOrder implements Controller {
    private final SalesAssistant salesAssistant;

    public PlaceOrder(SalesAssistant salesAssistant) {
        this.salesAssistant = salesAssistant;
    }

    public void process(Request request, Response response) throws Exception {
        OrderNumber orderNumber = salesAssistant.placeOrder(readPaymentDetailsFrom(request));
        response.redirectTo("/receipts/" + orderNumber.getNumber());
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
