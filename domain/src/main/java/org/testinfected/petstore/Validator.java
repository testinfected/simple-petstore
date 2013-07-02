package org.testinfected.petstore;

import java.util.Set;

public class Validator {

    public Set<ConstraintViolation<?>> validate(Object target) {
        Problems problems = new Problems();
        Check.valid(target).check(null, problems);
        return problems.violations();
    }
}