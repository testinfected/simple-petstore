package com.vtence.molecule.support;

import com.vtence.molecule.http.HttpMethod;
import com.vtence.molecule.Request;
import org.hamcrest.Matcher;

import static org.junit.Assert.assertThat;

public class MockRequest extends Request {

    public MockRequest() {
        uri("/");
        path("/");
        protocol("HTTP/1.1");
        method(HttpMethod.GET);
    }

    public static MockRequest GET(String path) {
        MockRequest request = new MockRequest();
        request.path(path);
        request.method(HttpMethod.GET);
        return request;
    }

    public static MockRequest POST(String path) {
        MockRequest request = new MockRequest();
        request.path(path);
        request.method(HttpMethod.POST);
        return request;
    }

    public static MockRequest PUT(String path) {
        MockRequest request = new MockRequest();
        request.path(path);
        request.method(HttpMethod.PUT);
        return request;
    }

    public static MockRequest DELETE(String path) {
        MockRequest request = new MockRequest();
        request.path(path);
        request.method(HttpMethod.DELETE);
        return request;
    }

    public void assertAttribute(Object key, Matcher<Object> attributeMatcher) {
        assertThat("attribute[" + key.toString() + "]", attribute(key), attributeMatcher);
    }

    public String toString() {
        return String.format("%s %s", method(), path());
    }
}