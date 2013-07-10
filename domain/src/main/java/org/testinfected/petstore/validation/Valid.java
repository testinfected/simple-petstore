package org.testinfected.petstore.validation;

import java.io.Serializable;
import java.lang.reflect.Field;

public class Valid<T> implements Serializable, Constraint<T> {

    private T value;

    public Valid(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public void check(Path path, Validation validation) {
        for (Field field : Constraints.of(value)) {
            Constraint constraint = Constraints.valueOf(value, field);
            constraint.check(path.node(field.getName()), validation);
        }
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
