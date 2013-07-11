package org.testinfected.petstore.validation;

import java.io.Serializable;

public class Valid<T> implements Serializable, Constraint<T> {

    private static final String INVALID = "invalid";

    private T value;
    private boolean rootViolationDisabled;

    public Valid(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public void check(Path path, Validation validation) {
        Problems problems = Problems.track(validation);
        for (Constraints.Property property : Constraints.of(value)) {
            property.check(path, problems);
        }
        if (problems.found && !rootViolationDisabled) validation.report(path, INVALID, value);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Valid notNull = (Valid) o;
        if (value != null ? !value.equals(notNull.value) : notNull.value != null) return false;
        return true;
    }

    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    public void disableRootViolation() {
        this.rootViolationDisabled = true;
    }

    public static class Problems implements Validation {

        private final Validation validation;
        private boolean found;

        public static Problems track(Validation validation) {
            return new Problems(validation);
        }

        public Problems(Validation validation) {
            this.validation = validation;
        }

        public <T> void report(Path path, String error, T offendingValue) {
            this.found = true;
            validation.report(path, error, offendingValue);
        }
    }
}
