package test.support.org.testinfected.petstore.web;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.Status;
import org.testinfected.petstore.Application;

public class StaticResponse implements Application {

    private int code;
    private String page;
    private String contentType = "text/html; charset=iso-8859-1";

    public static StaticResponse respondWith(Status status) {
        return respondWithCode(status.getCode());
    }

    public static StaticResponse respondWithCode(int code) {
        return new StaticResponse(code);
    }

    public static StaticResponse respondWith(Status code, String page) {
        return respondWith(code.getCode(), page);
    }

    public static StaticResponse respondWith(int code, String page) {
        return new StaticResponse(code, page);
    }

    public StaticResponse(int code) {
        this(code, "");
    }

    public StaticResponse(int code, String page) {
        this.code = code;
        this.page = page;
    }

    public void handle(Request request, Response response) throws Exception {
        response.setCode(code);
        response.setContentLength(contentLength());
        response.set("Content-Type", contentType);
        response.getPrintStream(contentLength()).print(page);
    }

    private int contentLength() {
        return page.getBytes().length;
    }
}
