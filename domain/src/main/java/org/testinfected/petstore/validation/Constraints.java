package org.testinfected.petstore.validation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class Constraints {

    public static Iterable<Declaration> of(Object target) {
        List<Declaration> constraints = new ArrayList<>();
        for (Field property : target.getClass().getDeclaredFields()) {
            if (isConstraint(property)) constraints.add(new Declaration(target, property));
        }
        return constraints;
    }

    private static boolean isConstraint(Field field) {
        return Constraint.class.isAssignableFrom(field.getType());
    }

    public static class Declaration {

        private final Object target;
        private final Field constraint;

        public Declaration(Object target, Field constraint) {
            this.target = target;
            this.constraint = constraint;
        }

        public String name() {
            return constraint.getName();
        }

		public Constraint<?> constraint() {
            try {
                constraint.setAccessible(true);
                return (Constraint<?>) constraint.get(target);
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException("Constraint " + constraint.getName() + " is not accessible on " + target);
            }
        }

        public void check(Path path, Report validation) {
            constraint().check(path.node(name()), validation);
        }
    }

    private Constraints() {}
}