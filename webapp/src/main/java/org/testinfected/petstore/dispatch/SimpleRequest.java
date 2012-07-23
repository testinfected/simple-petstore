package org.testinfected.petstore.dispatch;

import org.simpleframework.http.Request;

public class SimpleRequest implements Dispatch.Request {

    private final Request request;

    public SimpleRequest(Request request) {
        this.request = request;
    }

    public String getParameter(String name) {
        return null;
    }

    public String getPath() {
        return request.getPath().getPath();
    }

    public String getMethod() {
        return request.getMethod();
    }
}
