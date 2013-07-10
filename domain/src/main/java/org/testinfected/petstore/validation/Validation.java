package org.testinfected.petstore.validation;

public interface Validation {

    <T> void report(Path path, String error, T offendingValue);
}
