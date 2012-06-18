package test.support.org.testinfected.petstore.web;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.Status;
import org.testinfected.petstore.Application;

public class EmptyResponse implements Application {

    private final int code;

    public static Application respondWith(Status status) {
        return respondWithCode(status.getCode());
    }

    public static Application respondWithCode(int code) {
        return new EmptyResponse(code);
    }

    public EmptyResponse(int code) {
        this.code = code;
    }

    public void handle(Request request, Response response) throws Exception {
        response.setCode(code);
    }
}
