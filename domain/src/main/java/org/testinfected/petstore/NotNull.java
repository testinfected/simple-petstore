package org.testinfected.petstore;

import java.io.Serializable;

public class NotNull<T> implements Serializable, Constraint {

    private static final String MISSING = "missing";

    private T value;

    public NotNull(T value) {
        this.value = value;
    }

    public T get() {
        Check.satisfied(this);
        return value;
    }

    public void check(String path, Validation validation) {
        if (!satisfied()) validation.report(new ConstraintViolation<T>(path, MISSING, value));
    }

    private boolean satisfied() {
        return value != null;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NotNull notNull = (NotNull) o;

        if (value != null ? !value.equals(notNull.value) : notNull.value != null) return false;

        return true;
    }

    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
