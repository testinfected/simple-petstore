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
    public MockRequest(String path) {
        this();
        setPath(path);
    }

    private void setPath(String path) {
        this.path = path;
    }

    private void setMethod(HttpMethod method) {
        setMethod(method.name());
    }

    private void setMethod(String method) {
        this.method = method;
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

    public static Request POST(String path) {
        MockRequest request = new MockRequest(path);
        request.setMethod(HttpMethod.POST);
        return request;
    }

    public static Request GET(String path) {
        MockRequest request = new MockRequest(path);
        request.setMethod(HttpMethod.GET);
        return request;
    }
}
