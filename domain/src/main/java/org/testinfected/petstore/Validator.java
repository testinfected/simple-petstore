package org.testinfected.petstore;

import java.util.HashSet;
import java.util.Set;

public class Validator {

    public Set<ConstraintViolation<?>> validate(Object target) {
        return check(Validate.valid(target));
    }

    public Set<ConstraintViolation<?>> check(Constraint constraint) {
        Problems problems = new Problems();
        constraint.check(null, problems);
        return problems.violations;
    }

    class Problems implements Validation {

        final Set<ConstraintViolation<?>> violations = new HashSet<ConstraintViolation<?>>();

        public <T> void report(String path, String error, T offendingValue) {
            violations.add(new ConstraintViolation<Object>(path, error, offendingValue));
        }
    }
}