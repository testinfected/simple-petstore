package test.support.org.testinfected.petstore.web;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.Status;
import org.testinfected.petstore.Handler;

public class Empty implements Handler {

    private final Status status;
    
    public static Handler empty(Status status) {
        return new Empty(status);
    }

    public Empty(Status status) {
        this.status = status;
    }

    public void handle(Request request, Response response) throws Exception {
        response.setCode(status.getCode());
        response.setText(status.getDescription());
    }
}
