package org.testinfected.petstore;

import java.util.Set;

public class Validator {

    public Set<ConstraintViolation<?>> validate(Object target) {
        Problems problems = new Problems();
        Validate.valid(target).check(null, problems);
        return problems.violations();
    }
}