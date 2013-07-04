package org.testinfected.petstore.controllers;

import org.testinfected.molecule.Application;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;
import org.testinfected.petstore.Page;
import org.testinfected.petstore.Validator;
import org.testinfected.petstore.helpers.ErrorList;
import org.testinfected.petstore.helpers.FormErrors;
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
        FormErrors errors = paymentForm.validate(validator);
        if (errors.empty()) {
            OrderNumber orderNumber = salesAssistant.placeOrder(SessionScope.cartFor(request), paymentForm.paymentMethod());
            response.redirectTo("/orders/" + orderNumber.getNumber());
        } else {
            checkoutPage.render(response, context().with("errors", new ErrorList(errors)).asMap());
        }
    }
}
