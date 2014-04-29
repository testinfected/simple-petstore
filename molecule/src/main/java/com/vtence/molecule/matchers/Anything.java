package com.vtence.molecule.matchers;

import com.vtence.molecule.util.Matcher;

public class Anything<T> implements Matcher<T> {
    public boolean matches(T actual) {
        return true;
    }

    public static <T> Anything<T> anything() {
        return new Anything<T>();
    }
}
