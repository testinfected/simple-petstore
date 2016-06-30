package org.testinfected.petstore.validation;

import java.io.Serializable;

public class NotNull<T> implements Serializable, Constraint<T> {

    private static final String MISSING = "missing";

    private T value;

    public NotNull(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public void check(Path path, Report report) {
        if (!satisfied()) report.violation(path, MISSING, value);
    }

    private boolean satisfied() {
        return value != null;
    }

	public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NotNull<?> notNull = (NotNull) o;

        return value != null ? value.equals(notNull.value) : notNull.value == null;

    }

    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
