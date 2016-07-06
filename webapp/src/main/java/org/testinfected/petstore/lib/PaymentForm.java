package org.testinfected.petstore.lib;

import com.vtence.molecule.Request;
import org.testinfected.petstore.billing.Address;
import org.testinfected.petstore.billing.CreditCardDetails;
import org.testinfected.petstore.billing.CreditCardType;
import org.testinfected.petstore.validation.Valid;
import org.testinfected.petstore.validation.Validates;

import static org.testinfected.petstore.billing.CreditCardType.valueOf;

public class PaymentForm extends Form {

    public static PaymentForm parse(Request request) {
        return new PaymentForm(new CreditCardDetails(
                valueOf(request.parameter("card-type")),
                request.parameter("card-number"),
                request.parameter("expiry-date"),
                new Address(
                        request.parameter("first-name"),
                        request.parameter("last-name"),
                        request.parameter("email"))));
    }

    private final Valid<CreditCardDetails> paymentDetails;

    public PaymentForm(CreditCardDetails paymentDetails) {
        this.paymentDetails = Validates.validityOf(paymentDetails);
    }

    public CreditCardType cardType() {
        return paymentDetails().getCardType();
    }

    public CreditCardDetails paymentDetails() {
        return paymentDetails.get();
    }
}
