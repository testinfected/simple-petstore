package org.testinfected.petstore.controllers;

import com.vtence.molecule.Application;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import org.testinfected.petstore.Messages;
import org.testinfected.petstore.View;
import org.testinfected.petstore.lib.PaymentForm;
import org.testinfected.petstore.lib.SessionScope;
import org.testinfected.petstore.order.OrderNumber;
import org.testinfected.petstore.order.SalesAssistant;
import org.testinfected.petstore.validation.Validator;
import org.testinfected.petstore.views.Checkout;

import java.io.IOException;
import java.math.BigDecimal;

public class PlaceOrder implements Application {
    private final SalesAssistant salesAssistant;
    private final View<Checkout> checkoutView;
    private final Messages messages;
    private final Validator validator = new Validator();

    public PlaceOrder(SalesAssistant salesAssistant, View<Checkout> checkoutView, Messages messages) {
        this.salesAssistant = salesAssistant;
        this.checkoutView = checkoutView;
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
        OrderNumber orderNumber = salesAssistant.placeOrder(SessionScope.cart(request), form.paymentDetails());
        response.redirectTo("/orders/" + orderNumber.getNumber()).done();
    }

    private void rejectOrder(Request request, Response response, PaymentForm form) throws IOException {
        checkoutView.render(response, new Checkout().
                forTotalOf(currentCartTotal(request)).
                withPayment(form.paymentDetails()).
                withErrors(form.errors(messages)));
    }

    private BigDecimal currentCartTotal(Request request) {
        return SessionScope.cart(request).getGrandTotal();
    }
}