package org.testinfected.petstore.validation;

import java.util.HashSet;
import java.util.Set;

public class Validator {

    public <T> Set<ConstraintViolation<?>> validate(T target) {
        Valid<T> valid = Validate.valid(target);
        valid.disableRootViolation();
        Report report = new Report();
        valid.check(Path.root(target), report);
        return report.violations;
    }

    public static class Report implements Validation {

        final Set<ConstraintViolation<?>> violations = new HashSet<ConstraintViolation<?>>();

        public <T> void report(Path path, String error, T offendingValue) {
            violations.add(new ConstraintViolation<T>(path, error, offendingValue));
        }
    }
}