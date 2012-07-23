package test.support.org.testinfected.petstore.web;

import org.testinfected.petstore.dispatch.Dispatch;

import java.util.HashMap;
import java.util.Map;

public class MockRequest implements Dispatch.Request {

    private final Map<String, String> parameters = new HashMap<String, String>();
    private String path;
    private String method;

    public MockRequest() {
        this("GET", "/");
    }

    public MockRequest(String method, String path) {
        this.method = method;
        this.path = path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    
    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public String getMethod() {
        return method;
    }

    public String getParameter(String name) {
        return parameters.get(name);
    }

    public void addParameter(String name, String value) {
        parameters.put(name, value);
    }
}
