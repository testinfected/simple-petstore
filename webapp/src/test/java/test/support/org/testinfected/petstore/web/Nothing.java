package test.support.org.testinfected.petstore.web;

import org.testinfected.petstore.util.Matcher;

public class Nothing<T> implements Matcher<T> {
    public boolean matches(T actual) {
        return false;
    }
}
