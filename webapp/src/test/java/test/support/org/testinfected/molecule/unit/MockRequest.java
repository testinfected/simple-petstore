package test.support.org.testinfected.molecule.unit;

import org.hamcrest.Matcher;
import org.testinfected.molecule.HttpMethod;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.Session;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertThat;

public class MockRequest implements Request {

    private final Map<String, String> params = new HashMap<String, String>();
    private final Map<Object, Object> attributes = new HashMap<Object, Object>();
    private final Map<String, String> cookies = new HashMap<String, String>();
    private HttpMethod method = HttpMethod.GET;
    private String path = "/";
    private String ip = "127.0.0.1";
    private String protocol = "HTTP/1.1";
    private Session session;

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

    public MockRequest withIp(String address) {
        this.ip = address;
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
        return protocol;
    }

    public String uri() {
        return pathInfo();
    }

    public <T> T unwrap(Class<T> type) {
        throw new UnsupportedOperationException();
    }

    public String ip() {
        return ip;
    }

    public Object attribute(Object key) {
        return attributes.get(key);
    }

    public void attribute(Object key, Object value) {
        attributes.put(key, value);
    }

    public void removeAttribute(Object key) {
        attributes.remove(key);
    }

    public void assertAttribute(Object key, Matcher<Object> attributeMatcher) {
        assertThat("attribute["  + key.toString() + "]", attribute(key), attributeMatcher);
    }

    public void withCookie(String name, String value) {
        cookies.put(name, value);
    }

    public String cookie(String name) {
        return cookies.get(name);
    }

    public Session session() {
        return session(true);
    }

    public Session session(boolean create) {
        if (session == null && create) {
            session = new MockSession();
        }
        return session;
    }

    public String toString() {
        return String.format("%s %s", method(), pathInfo());
    }
}
