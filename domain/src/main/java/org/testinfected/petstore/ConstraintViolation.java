package org.testinfected.petstore;

public class ConstraintViolation<T> {

    private final String path;
    private final String message;
    private final T offendingValue;

    public ConstraintViolation(String path, String message, T offendingValue) {
        this.path = path;
        this.message = message;
        this.offendingValue = offendingValue;
    }

    public String path() {
        return path;
    }

    public String message() {
        return message;
    }

    public T value() {
        return offendingValue;
    }
}
