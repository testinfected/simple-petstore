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

    public void check(Path path, Report report) {
        Problems problems = Problems.track(report);
        for (Constraints.Declaration constraint : Constraints.of(value)) {
            constraint.check(path, problems);
        }
        if (reportViolationOnRoot(problems)) report.violation(path, INVALID, value);
    }

    private boolean reportViolationOnRoot(Problems problems) {
        return problems.found && !rootViolationDisabled;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

		Valid<?> notNull = (Valid) o;
        return value != null ? value.equals(notNull.value) : notNull.value == null;
    }

    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    public void disableRootViolation() {
        this.rootViolationDisabled = true;
    }

    public static class Problems implements Report {

        private final Report validation;
        private boolean found;

        public static Problems track(Report validation) {
            return new Problems(validation);
        }

        public Problems(Report validation) {
            this.validation = validation;
        }

        public <T> void violation(Path path, String error, T offendingValue) {
            this.found = true;
            validation.violation(path, error, offendingValue);
        }
    }
}
