package com.vtence.molecule.http;

public class Cookie {
    private final String name;

    private String value;
    private boolean httpOnly;
    private int maxAge = -1;

    public Cookie(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String name() {
        return name;
    }

    public String value() {
        return value;
    }

    public boolean httpOnly() {
        return httpOnly;
    }

    public Cookie httpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
        return this;
    }

    public Cookie maxAge(int maxAge) {
        this.maxAge = maxAge;
        return this;
    }

    public int maxAge() {
        return maxAge;
    }

    public String toString() {
        return name + "=" + value +
                (maxAge >= 0 ? "Max-Age=" + maxAge : "") +
                (httpOnly ? "; HttpOnly" : "");
    }
}
