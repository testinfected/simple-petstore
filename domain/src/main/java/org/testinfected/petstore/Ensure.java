package org.testinfected.petstore;

import java.util.Set;

public final class Ensure {

    public static void valid(Object target) {
        Ensure.satisfied(Validate.valid(target));
    }

    public static void satisfied(Constraint constraint) {
        Validator validator = new Validator();
        Set<ConstraintViolation<?>> violations = validator.check(constraint);
        if (!violations.isEmpty()) throw new ConstraintViolationException(violations);
    }

    private Ensure() {}
}
