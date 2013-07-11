package org.testinfected.petstore.validation;

public interface Validation {

    <T> void reportViolation(Path path, String error, T offendingValue);
}
