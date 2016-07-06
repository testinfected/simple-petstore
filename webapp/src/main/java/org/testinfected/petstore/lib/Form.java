package org.testinfected.petstore.lib;

import org.testinfected.petstore.Messages;
import org.testinfected.petstore.validation.ConstraintViolation;
import org.testinfected.petstore.validation.Validator;

import java.util.HashSet;
import java.util.Set;

public abstract class Form {

    private final Set<ConstraintViolation<?>> violations = new HashSet<>();

    public boolean validate(Validator validator) {
        violations.addAll(validator.validate(this));
        return violations.isEmpty();
    }

    public ErrorMessages errors(Messages messages) {
        ErrorMessages errors = new ErrorMessages();
        for (ConstraintViolation<?> violation : violations) {
            errors.add(violation.path(), translate(messages, violation.path(), violation.error()));
        }
        return errors;
    }

    private String translate(Messages messages, String path, String error) {
        return messages.interpolate(error + "." + path);
    }
}
