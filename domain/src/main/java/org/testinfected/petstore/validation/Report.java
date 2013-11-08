package org.testinfected.petstore.validation;

public interface Report {

    <T> void violation(Path path, String error, T offendingValue);
}
