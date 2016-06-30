package org.testinfected.petstore.validation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Validator {

    public <T> Set<ConstraintViolation<?>> validate(T target) {
        Valid<T> valid = Validates.validityOf(target);
        valid.disableRootViolation();
        ViolationsReport report = new ViolationsReport();
        valid.check(Path.root(target), report);
        return report.violations();
    }

    public static class ViolationsReport implements Report {

        private final Set<ConstraintViolation<?>> violations = new HashSet<>();

        public <T> void violation(Path path, String error, T offendingValue) {
            violations.add(new ConstraintViolation<>(path, error, offendingValue));
        }

        public Set<ConstraintViolation<?>> violations() {
            return Collections.unmodifiableSet(violations);
        }
    }
}