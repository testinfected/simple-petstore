package org.testinfected.petstore;

import java.util.Collections;
import java.util.Set;

public class ConstraintViolationException extends IllegalArgumentException {

    private final Set<ConstraintViolation<?>> violations;

    public ConstraintViolationException(Set<ConstraintViolation<?>> violations) {
        this.violations = violations;
    }

    public Set<ConstraintViolation<?>> violations() {
        return Collections.unmodifiableSet(violations);
    }
}
