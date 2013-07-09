package org.testinfected.petstore.validation;

import java.util.Set;

public final class Ensure {

    public static void valid(Object target) {
        Validator validator = new Validator();
        Set<ConstraintViolation<?>> violations = validator.validate(target);
        if (!violations.isEmpty()) throw new ConstraintViolationException(violations);
    }

    private Ensure() {}
}
