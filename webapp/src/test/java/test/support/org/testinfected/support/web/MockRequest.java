package test.support.org.testinfected.support.web;

import org.testinfected.support.HttpMethod;
import org.testinfected.support.Request;
import org.testinfected.support.Session;

import java.util.HashMap;
import java.util.Map;

public class MockRequest implements Request {

    private final Map<String, String> params = new HashMap<String, String>();
    private String method;
    private String path;

    public static MockRequest aRequest() {
        return new MockRequest();
    }

    public static Request POST(String path) {
        return aRequest().withPath(path).withMethod(HttpMethod.POST);
    }

    public static Request GET(String path) {
        return aRequest().withPath(path).withMethod(HttpMethod.GET);
    }

    public MockRequest() {}

    public MockRequest withPath(String path) {
        this.path = path;
        return this;
    }

    public MockRequest withMethod(HttpMethod method) {
        return withMethod(method.name());
    }

    public MockRequest withMethod(String method) {
        this.method = method;
        return this;
    }

    public String method() {
        return method;
    }

    public String pathInfo() {
        return path;
    }

    public void addParameter(String name, String value) {
        params.put(name, value);
    }

    public String parameter(String name) {
        return params.get(name);
    }

    public String protocol() {
        return null;
    }

    public String uri() {
        return null;
    }

    // todo remove when transition to request/response complete
    public <T> T unwrap(Class<T> type) {
        return (T) new MockSimpleRequest().withMethod(method).withPath(path);
    }

    public String ip() {
        return null;
    }

    public Object attribute(Object key) {
        return null;
    }

    public void attribute(Object key, Object value) {
    }

    public void removeAttribute(Object key) {
    }

    public Session session() {
        return null;
    }
}
