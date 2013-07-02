package org.testinfected.petstore;

import java.io.Serializable;
import java.lang.reflect.Field;

public class Valid<T> implements Serializable, Constraint {

    private static final String INVALID = "invalid";

    private T value;

    public Valid(T value) {
        this.value = value;
    }

    public T get() {
        if (!satisfied()) throw new IllegalArgumentException(INVALID);
        return value;
    }

    public void check(String path, Validation validation) {
        for (Field field : Constraints.of(value)) {
            Constraint constraint = Constraints.valueOf(value, field);
            constraint.check(node(path, field.getName()), validation);
        }
    }

    // TODO We might need better than a string for representing the path in the object graph
    private String node(String path, String name) {
        return path != null ? path + "." + name : name;
    }

    private boolean satisfied() {
        Problems problems = new Problems();
        check(null, problems);
        return problems.isEmpty();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Valid notNull = (Valid) o;

        if (value != null ? !value.equals(notNull.value) : notNull.value != null) return false;

        return true;
    }

    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
