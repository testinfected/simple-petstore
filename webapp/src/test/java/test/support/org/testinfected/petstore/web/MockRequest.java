package test.support.org.testinfected.petstore.web;

import org.simpleframework.http.Path;
import org.simpleframework.http.Request;
import org.simpleframework.http.RequestWrapper;
import org.simpleframework.http.parse.PathParser;

//todo use where possible instead of using dynamic mocks
public class MockRequest extends RequestWrapper {

    private static final Request DUMMY_REQUEST = null;
    private static final String POST = "POST";

    private String method;
    private String path;

    public MockRequest() {
        super(DUMMY_REQUEST);
    }

    public static Request post(String path) {
        MockRequest request = new MockRequest();
        request.setMethod(POST);
        request.setPath(path);
        return request;
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
}
