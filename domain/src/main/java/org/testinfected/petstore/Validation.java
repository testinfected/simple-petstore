package org.testinfected.petstore;

public interface Validation {

    <T> void report(ConstraintViolation<T> violation);
}
