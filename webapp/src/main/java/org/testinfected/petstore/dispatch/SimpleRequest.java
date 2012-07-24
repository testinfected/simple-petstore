package org.testinfected.petstore.dispatch;

import org.simpleframework.http.Request;
import org.testinfected.petstore.ExceptionImposter;

import java.io.IOException;

public class SimpleRequest implements Dispatch.Request {

    private final Request request;

    public SimpleRequest(Request request) {
        this.request = request;
    }

    public String getParameter(String name) {
        try {
            return request.getParameter(name);
        } catch (IOException e) {
            throw ExceptionImposter.imposterize(e);
        }
    }

    public String getPath() {
        return request.getPath().getPath();
    }

    public String getMethod() {
        return request.getMethod();
    }
}
