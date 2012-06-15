package test.support.org.testinfected.petstore.web;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.Status;
import org.testinfected.petstore.Handler;

public class EmptyResponse implements Handler {

    private final int code;

    public static Handler respondWith(Status status) {
        return respondWithCode(status.getCode());
    }

    public static Handler respondWithCode(int code) {
        return new EmptyResponse(code);
    }

    public EmptyResponse(int code) {
        this.code = code;
    }

    public void handle(Request request, Response response) throws Exception {
        response.setCode(code);
    }
}
