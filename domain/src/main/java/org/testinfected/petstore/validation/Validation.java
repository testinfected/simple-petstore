package org.testinfected.petstore.validation;

public interface Validation {

    <T> void report(String path, String error, T offendingValue);
}
