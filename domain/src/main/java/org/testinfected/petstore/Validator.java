package org.testinfected.petstore;

import java.lang.reflect.Field;
import java.util.Set;

public class Validator {

    public Set<ConstraintViolation<?>> validate(Object target) {
        Problems problems = new Problems();
        for (Field field : declaredFieldsOf(target)) {
            if (isConstraint(field)) {
                Constraint constraint = (Constraint) valueOf(target, field);
                constraint.check(field.getName(), problems);
            }
        }
        return problems.violations();
    }

    private boolean isConstraint(Field field) {
        return Constraint.class.isAssignableFrom(field.getType());
    }

    private Field[] declaredFieldsOf(Object target) {
        return target.getClass().getDeclaredFields();
    }

    private Object valueOf(Object target, Field field) {
        try {
            field.setAccessible(true);
            return field.get(target);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Field " + field.getName() + " is not accessible");
        }
    }
}