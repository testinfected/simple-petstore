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

    public void check(Path path, Report report) {
        left.check(path, report);
        right.check(path, report);
    }
}
