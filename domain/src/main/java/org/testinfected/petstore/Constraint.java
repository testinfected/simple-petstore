package org.testinfected.petstore;

public interface Constraint<T> {

    T get();

    void check(String path, Validation validation);
}
