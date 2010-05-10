package com.pyxis.petstore.domain;

public class Maybe<T> {
    private final T value;

    public static <T> Maybe<T> maybe(T value) {
        return new Maybe<T>(value);
    }

    public Maybe(T value) {
        this.value = value;
    }

    public boolean exists() {
        return value != null;
    }

    public T value() {
        return value;
    }
}
