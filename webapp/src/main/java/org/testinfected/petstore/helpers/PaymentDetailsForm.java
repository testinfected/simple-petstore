package org.testinfected.petstore.helpers;

import org.testinfected.molecule.Request;
import org.testinfected.petstore.ConstraintViolation;
import org.testinfected.petstore.Validator;
import org.testinfected.petstore.billing.Address;
import org.testinfected.petstore.billing.CreditCardDetails;
import org.testinfected.petstore.billing.PaymentMethod;

import java.util.Set;

import static org.testinfected.petstore.billing.CreditCardType.valueOf;

public class PaymentDetailsForm {
    private final CreditCardDetails cardDetails;

    public PaymentDetailsForm(Request request) {
        this.cardDetails = new CreditCardDetails(
                        valueOf(request.parameter("card-type")),
                        request.parameter("card-number"),
                        request.parameter("expiry-date"),
                        new Address(
                                request.parameter("first-name"),
                                request.parameter("last-name"),
                                request.parameter("email")));
    }

    public FormErrors validate(Validator validator) {
        Set<ConstraintViolation<?>> violations = validator.validate(paymentMethod());
        FormErrors errors = new FormErrors("payment");
        if (!violations.isEmpty()) {
            errors.reject("invalid");
            for (ConstraintViolation<?> violation : violations) {
                errors.rejectValue(violation.path(), violation.message());
            }
        }
        return errors;
    }

    public PaymentMethod paymentMethod() {
        return cardDetails;
    }
}
