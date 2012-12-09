package test.support.org.testinfected.molecule.web;

import org.testinfected.molecule.HttpMethod;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.Session;

import java.util.HashMap;
import java.util.Map;

public class MockRequest implements Request {

    private final Map<String, String> params = new HashMap<String, String>();
    private HttpMethod method = HttpMethod.GET;
    private String path = "/";

    public static MockRequest aRequest() {
        return new MockRequest();
    }

    public static MockRequest POST(String path) {
        return aRequest().withPath(path).withMethod(HttpMethod.POST);
    }

    public static MockRequest GET(String path) {
        return aRequest().withPath(path).withMethod(HttpMethod.GET);
    }

    public MockRequest() {}

    public MockRequest withPath(String path) {
        this.path = path;
        return this;
    }

    public MockRequest withMethod(HttpMethod method) {
        this.method = method;
        return this;
    }

    public HttpMethod method() {
        return method;
    }

    public String pathInfo() {
        return path;
    }

    public void addParameter(String name, String value) {
        params.put(name, value);
    }

    public MockRequest withParameter(String name, String value) {
        addParameter(name, value);
        return this;
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

    public <T> T unwrap(Class<T> type) {
        return null;
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

    public String toString() {
        return String.format("%s %s", method(), pathInfo());
    }
}
