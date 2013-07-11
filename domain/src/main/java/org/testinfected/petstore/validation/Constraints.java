package org.testinfected.petstore.validation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class Constraints {

    public static Iterable<Property> of(Object target) {
        List<Property> constraints = new ArrayList<Property>();
        for (Field property : target.getClass().getDeclaredFields()) {
            if (Property.isConstraint(property)) constraints.add(new Property(target, property));
        }
        return constraints;
    }

    public static class Property {

        private final Object target;
        private final Field constraint;

        public static boolean isConstraint(Field field) {
            return Constraint.class.isAssignableFrom(field.getType());
        }

        public Property(Object target, Field constraint) {
            this.target = target;
            this.constraint = constraint;
        }

        public String name() {
            return constraint.getName();
        }

        public Constraint constraint() {
            try {
                constraint.setAccessible(true);
                return (Constraint) constraint.get(target);
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException("Constraint " + constraint.getName() + " is not accessible on " + target);
            }
        }

        public void check(Path path, Validation validation) {
            constraint().check(path.node(name()), validation);
        }
    }

    private Constraints() {}
}