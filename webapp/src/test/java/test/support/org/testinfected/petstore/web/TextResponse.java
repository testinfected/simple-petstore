package test.support.org.testinfected.petstore.web;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.Status;
import org.testinfected.petstore.Application;

public class TextResponse implements Application {

    private final int code;
    private final String text;

    public TextResponse(int code) {
        this(code, "");
    }

    public TextResponse(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public static Application respondWith(Status status) {
        return respondWithCode(status.getCode());
    }

    public static Application respondWithCode(int code) {
        return new TextResponse(code);
    }

    public static Application respondWith(Status code, String text) {
        return respondWith(code.getCode(), text);
    }

    public static Application respondWith(int code, String text) {
        return new TextResponse(code, text);
    }

    public void handle(Request request, Response response) throws Exception {
        response.setCode(code);
        response.setContentLength(contentLength());
        response.getPrintStream(contentLength()).print(text);
    }

    private int contentLength() {
        return text.getBytes().length;
    }
}
