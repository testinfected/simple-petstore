package org.testinfected.petstore;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Problems implements Validation {

    private final Set<ConstraintViolation<?>> violations = new HashSet<ConstraintViolation<?>>();

    public <T> void report(ConstraintViolation<T> violation) {
        violations.add(violation);
    }

    public Set<ConstraintViolation<?>> violations() {
        return Collections.unmodifiableSet(violations);
    }
}
