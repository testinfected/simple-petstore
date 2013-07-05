package test.support.org.testinfected.petstore.web;

import org.testinfected.molecule.Request;
import org.testinfected.petstore.helpers.Form;

public class MockForm<T> extends Form<T>  {

    private T value;

    public static <T> MockForm<T> named(String name) {
        return new MockForm<T>(name);
    }

    public MockForm(String name) {
        super(name);
    }

    public MockForm<T> withValue(T value) {
        this.value = value;
        return this;
    }

    protected T parse(Request request) {
        return value;
    }
}
