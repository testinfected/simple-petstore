package org.testinfected.petstore.validation;

public interface Constraint<T> {

    T get();

    void check(String path, Validation validation);
}
