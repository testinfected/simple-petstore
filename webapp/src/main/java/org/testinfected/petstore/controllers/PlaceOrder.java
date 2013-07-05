package org.testinfected.petstore.controllers;

import org.testinfected.molecule.Application;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;
import org.testinfected.petstore.Page;
import org.testinfected.petstore.Validator;
import org.testinfected.petstore.helpers.ChoiceOfCreditCards;
import org.testinfected.petstore.helpers.ErrorList;
import org.testinfected.petstore.helpers.PaymentDetailsForm;
import org.testinfected.petstore.order.OrderNumber;
import org.testinfected.petstore.order.SalesAssistant;
import org.testinfected.petstore.util.SessionScope;

import static org.testinfected.petstore.util.Context.context;

public class PlaceOrder implements Application {
    private final SalesAssistant salesAssistant;
    private final Page checkoutPage;
    private final Validator validator = new Validator();

    public PlaceOrder(SalesAssistant salesAssistant, Page checkoutPage) {
        this.salesAssistant = salesAssistant;
        this.checkoutPage = checkoutPage;
    }

    public void handle(Request request, Response response) throws Exception {
        PaymentDetailsForm paymentForm = new PaymentDetailsForm(request);
        if (paymentForm.validate(validator)) {
            OrderNumber orderNumber = salesAssistant.placeOrder(SessionScope.cartFor(request), paymentForm.value());
            response.redirectTo("/orders/" + orderNumber.getNumber());
        } else {
            checkoutPage.render(response, context().
                    with("total", SessionScope.cartFor(request).getGrandTotal()).
                    and("cardTypes", ChoiceOfCreditCards.all().select(paymentForm.value().getCardType())).
                    and("payment", paymentForm.value()).
                    and("errors", new ErrorList(paymentForm.errors())).asMap());
        }
    }
}
