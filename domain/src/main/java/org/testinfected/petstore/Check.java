package org.testinfected.petstore;

public final class Check {

    public static void valid(Object target) {
        Check.satisfied(Validate.valid(target));
    }

    public static void satisfied(Constraint constraint) {
        Problems problems = new Problems();
        constraint.check(null, problems);
        if (!problems.isEmpty()) throw new ConstraintViolationException(problems.violations());
    }

    private Check() {}
}
