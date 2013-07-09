package org.testinfected.petstore.validation;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class Constraints implements Serializable {

    public static Iterable<Field> of(Object target) {
        List<Field> constraints = new ArrayList<Field>();
        for (Field field : target.getClass().getDeclaredFields()) {
            if (isConstraint(field)) constraints.add(field);
        }
        return constraints;
    }

    public static Constraint valueOf(Object target, Field constraint) {
        try {
            constraint.setAccessible(true);
            return (Constraint) constraint.get(target);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Constraint " + constraint.getName() + " is not accessible");
        }
    }

    private static boolean isConstraint(Field field) {
        return Constraint.class.isAssignableFrom(field.getType());
    }

    private Constraints() {}
}