package org.testinfected.petstore;

public interface Validation {

    <T> void report(String path, String error, T offendingValue);
}
