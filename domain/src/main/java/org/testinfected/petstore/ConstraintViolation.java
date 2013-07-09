package org.testinfected.petstore;

public class ConstraintViolation<T> {

    private final String path;
    private final String error;
    private final T offendingValue;

    public ConstraintViolation(String path, String error, T offendingValue) {
        this.path = path;
        this.error = error;
        this.offendingValue = offendingValue;
    }

    public String path() {
        return path;
    }

    public String error() {
        return error;
    }

    public T value() {
        return offendingValue;
    }

    public String toString() {
        return String.format("%s is %s: %s", path, error, offendingValue);
    }
}
