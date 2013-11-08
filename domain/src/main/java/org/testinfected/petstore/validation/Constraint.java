package org.testinfected.petstore.validation;

public interface Constraint<T> {

    T get();

    void check(Path path, Report report);
}
