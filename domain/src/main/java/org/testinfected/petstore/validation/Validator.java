package org.testinfected.petstore.validation;

import java.util.HashSet;
import java.util.Set;

public class Validator {

    public <T> Set<ConstraintViolation<?>> validate(T target) {
        Valid<T> constraint = Validate.valid(target);
        Problems problems = new Problems();
        constraint.check(Path.root(target), problems);
        return problems.violations;
    }

    class Problems implements Validation {

        final Set<ConstraintViolation<?>> violations = new HashSet<ConstraintViolation<?>>();

        public <T> void report(Path path, String error, T offendingValue) {
            violations.add(new ConstraintViolation<T>(path, error, offendingValue));
        }
    }
}