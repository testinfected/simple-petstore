package test.support.org.testinfected.petstore.web;

import org.simpleframework.http.Path;
import org.simpleframework.http.Request;
import org.simpleframework.http.RequestWrapper;
import org.simpleframework.http.parse.PathParser;
import org.testinfected.petstore.util.HttpMethod;

//todo use where possible instead of using dynamic mocks
public class MockRequest extends RequestWrapper {

    private static final Request DUMMY_REQUEST = null;

    private String method;
    private String path;

    public MockRequest(HttpMethod method) {
        super(DUMMY_REQUEST);
        setMethod(method.name());
    }

    private void setPath(String path) {
        this.path = path;
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

    public static Request POST(String path) {
        MockRequest request = new MockRequest(HttpMethod.POST);
        request.setPath(path);
        return request;
    }
}
