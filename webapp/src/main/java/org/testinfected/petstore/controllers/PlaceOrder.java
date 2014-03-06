package org.testinfected.petstore.controllers;

import org.testinfected.molecule.Application;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;
import org.testinfected.petstore.Page;
import org.testinfected.petstore.helpers.ChoiceOfCreditCards;
import org.testinfected.petstore.helpers.ListOfErrors;
import org.testinfected.petstore.helpers.Messages;
import org.testinfected.petstore.helpers.PaymentForm;
import org.testinfected.petstore.order.OrderNumber;
import org.testinfected.petstore.order.SalesAssistant;
import org.testinfected.petstore.util.SessionScope;
import org.testinfected.petstore.validation.Validator;

import java.io.IOException;

import static org.testinfected.petstore.util.Context.context;

public class PlaceOrder implements Application {
    private final SalesAssistant salesAssistant;
    private final Page checkoutPage;
    private final Messages messages;
    private final Validator validator = new Validator();

    public PlaceOrder(SalesAssistant salesAssistant, Page checkoutPage, Messages messages) {
        this.salesAssistant = salesAssistant;
        this.checkoutPage = checkoutPage;
        this.messages = messages;
    }

    public void handle(Request request, Response response) throws Exception {
        PaymentForm form = PaymentForm.parse(request);
        if (valid(form)) {
            processOrder(request, response, form);
        } else {
            rejectOrder(request, response, form);
        }
    }

    private boolean valid(PaymentForm form) {
        return form.validate(validator);
    }

    private void processOrder(Request request, Response response, PaymentForm form) throws Exception {
        OrderNumber orderNumber = salesAssistant.placeOrder(SessionScope.cartFor(request), form.paymentDetails());
        response.redirectTo("/orders/" + orderNumber.getNumber());
    }

    private void rejectOrder(Request request, Response response, PaymentForm form) throws IOException {
        checkoutPage.render(response, context().
                with("total", SessionScope.cartFor(request).getGrandTotal()).
                and("cardTypes", ChoiceOfCreditCards.all().select(form.cardType())).
                and("payment", form.paymentDetails()).
                and("errors", new ListOfErrors(form.errors(messages))).asMap());
    }
}
