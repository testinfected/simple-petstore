package org.testinfected.petstore.validation;

import java.io.Serializable;

public class Both<T> implements Constraint<T>, Serializable {

    private final Constraint<T> left;
    private final Constraint<T> right;

    public Both(Constraint<T> left, Constraint<T> right) {
        this.left = left;
        this.right = right;
    }

    public T get() {
        return left.get();
    }

    public void check(Path path, Validation validation) {
        left.check(path, validation);
        right.check(path, validation);
    }
}
