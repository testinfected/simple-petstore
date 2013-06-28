package org.testinfected.petstore;

import java.io.Serializable;

public class NotNull<T> implements Serializable, Constraint {

    private static final String MISSING = "missing";

    private T value;

    public NotNull(T value) {
        this.value = value;
    }

    public T get() {
        if (!satisfied()) throw new IllegalArgumentException(MISSING);
        return value;
    }

    public void check(String path, Validation validation) {
        if (!satisfied()) validation.report(new ConstraintViolation<T>(path, MISSING, value));
    }

    private boolean satisfied() {
        return value != null;
    }
}
