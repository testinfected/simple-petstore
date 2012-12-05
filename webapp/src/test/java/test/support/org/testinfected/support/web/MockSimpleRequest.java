package test.support.org.testinfected.support.web;

import org.simpleframework.http.Path;
import org.simpleframework.http.Request;
import org.simpleframework.http.RequestWrapper;
import org.simpleframework.http.parse.PathParser;
import org.testinfected.support.HttpMethod;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MockSimpleRequest extends RequestWrapper {

    private static final Request DUMMY_REQUEST = null;

    private final Map<String, String> params = new HashMap<String, String>();
    private String method;
    private String path;

    public MockSimpleRequest() {
        super(DUMMY_REQUEST);
    }

    public MockSimpleRequest withPath(String path) {
        this.path = path;
        return this;
    }

    public MockSimpleRequest withMethod(HttpMethod method) {
        return withMethod(method.name());
    }

    public MockSimpleRequest withMethod(String method) {
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

    public static MockSimpleRequest aRequest() {
        return new MockSimpleRequest();
    }

    public static Request POST(String path) {
        return aRequest().withPath(path).withMethod(HttpMethod.POST);
    }

    public static Request GET(String path) {
        return aRequest().withPath(path).withMethod(HttpMethod.GET);
    }
}
