package test.support.org.testinfected.petstore.web;

import org.simpleframework.http.Path;
import org.simpleframework.http.Request;
import org.simpleframework.http.RequestWrapper;
import org.simpleframework.http.parse.PathParser;
import org.testinfected.petstore.util.HttpMethod;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MockRequest extends RequestWrapper {

    private static final Request DUMMY_REQUEST = null;

    private final Map<String, String> params = new HashMap<String, String>();
    private String method;
    private String path;

    public MockRequest() {
        super(DUMMY_REQUEST);
    }

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

    public String getMethod() {
        return method;
    }

    public Path getPath() {
        return new PathParser(path);
    }

    public String getParameter(String name) throws IOException {
        return params.get(name);
    }

    public static MockRequest aRequest() {
        return new MockRequest();
    }

    public static Request POST(String path) {
        return aRequest().withPath(path).withMethod(HttpMethod.POST);
    }

    public static Request GET(String path) {
        return aRequest().withPath(path).withMethod(HttpMethod.GET);
    }
}
