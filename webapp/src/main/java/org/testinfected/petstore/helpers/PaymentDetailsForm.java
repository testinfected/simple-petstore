package org.testinfected.petstore.helpers;

import org.testinfected.molecule.Request;
import org.testinfected.petstore.ConstraintViolation;
import org.testinfected.petstore.Validator;
import org.testinfected.petstore.billing.Address;
import org.testinfected.petstore.billing.CreditCardDetails;

import java.util.Set;

import static org.testinfected.petstore.billing.CreditCardType.valueOf;

public class PaymentDetailsForm {
    private final CreditCardDetails cardDetails;
    private final FormErrors errors = new FormErrors("payment");

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

    public boolean validate(Validator validator) {
        Set<ConstraintViolation<?>> violations = validator.validate(value());
        if (!violations.isEmpty()) {
            errors.reject("invalid");
            for (ConstraintViolation<?> violation : violations) {
                errors.rejectValue(violation.path(), violation.message());
            }
        }
        return errors.empty();
    }

    public FormErrors errors() {
        return errors;
    }

    public CreditCardDetails value() {
        return cardDetails;
    }
}
