package org.testinfected.petstore;

public interface Constraint {

    void check(String path, Validation validation);
}
