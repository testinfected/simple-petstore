package org.testinfected.petstore.controllers;

import org.testinfected.molecule.Application;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;
import org.testinfected.petstore.Page;
import org.testinfected.petstore.Validator;
import org.testinfected.petstore.billing.Address;
import org.testinfected.petstore.billing.CreditCardDetails;
import org.testinfected.petstore.helpers.ChoiceOfCreditCards;
import org.testinfected.petstore.helpers.Errors;
import org.testinfected.petstore.helpers.Form;
import org.testinfected.petstore.order.OrderNumber;
import org.testinfected.petstore.order.SalesAssistant;
import org.testinfected.petstore.util.SessionScope;

import static org.testinfected.petstore.billing.CreditCardType.valueOf;
import static org.testinfected.petstore.util.Context.context;

public class PlaceOrder implements Application {
    private final SalesAssistant salesAssistant;
    private final Page checkoutPage;
    private final Form<CreditCardDetails> paymentForm = new PaymentForm();
    private final Validator validator = new Validator();

    public PlaceOrder(SalesAssistant salesAssistant, Page checkoutPage) {
        this.salesAssistant = salesAssistant;
        this.checkoutPage = checkoutPage;
    }

    public void handle(Request request, Response response) throws Exception {
        paymentForm.load(request);
        if (paymentForm.validate(validator)) {
            OrderNumber orderNumber = salesAssistant.placeOrder(SessionScope.cartFor(request), paymentForm.value());
            response.redirectTo("/orders/" + orderNumber.getNumber());
        } else {
            paymentForm.reject("invalid");
            checkoutPage.render(response, context().
                    with("total", SessionScope.cartFor(request).getGrandTotal()).
                    and("cardTypes", ChoiceOfCreditCards.all().select(paymentForm.value().getCardType())).
                    and("payment", paymentForm.value()).
                    and("errors", new Errors(paymentForm)).asMap());
        }
    }

    private class PaymentForm extends Form<CreditCardDetails> {
        private PaymentForm() {
            super("payment");
        }

        protected CreditCardDetails parse(Request request) {
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
}
