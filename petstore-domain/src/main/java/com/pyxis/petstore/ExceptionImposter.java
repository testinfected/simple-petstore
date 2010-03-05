package com.pyxis.petstore;

public class ExceptionImposter extends RuntimeException {
    private final Exception imposterized;

    public static RuntimeException imposterize(Exception e) {
        if (e instanceof RuntimeException) return (RuntimeException) e;

        return new ExceptionImposter(e);
    }

    public ExceptionImposter(Exception e) {
        super(e.getMessage(), e.getCause());
        imposterized = e;
        setStackTrace(e.getStackTrace());
    }

    public Exception getRealException() {
        return imposterized;
    }

    public String toString() {
        return imposterized.toString();
    }

}
