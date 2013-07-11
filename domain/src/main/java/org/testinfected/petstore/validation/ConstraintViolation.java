package org.testinfected.petstore.validation;

public class ConstraintViolation<T> {

    private final Path path;
    private final String error;
    private final T offendingValue;

    public ConstraintViolation(Path path, String error, T offendingValue) {
        this.path = path;
        this.error = error;
        this.offendingValue = offendingValue;
    }

    public String path() {
        return path.value();
    }

    public Object target() {
        return path.target();
    }

    public String error() {
        return error;
    }

    public T value() {
        return offendingValue;
    }

    public String toString() {
        return String.format("ConstraintViolation[target=%s, path=%s, value=%s, error=%s]", target(), path(), value(), error());
    }
}
