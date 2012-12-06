package test.support.org.testinfected.support.web;

import org.testinfected.support.*;

import java.nio.charset.Charset;

public class StaticResponse implements Application {

    private int code;
    private String page;
    private String encoding = "iso-8859-1";

    public static StaticResponse respondWith(HttpStatus status) {
        return respondWithCode(status.code);
    }

    public static StaticResponse respondWithCode(int code) {
        return new StaticResponse(code);
    }

    public static StaticResponse respondWith(HttpStatus status, String page) {
        return respondWith(status.code, page);
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

    public void setContentEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void handle(org.simpleframework.http.Request request, org.simpleframework.http.Response response) throws Exception {
        handle(new SimpleRequest(request), new SimpleResponse(response, null, Charset.defaultCharset()));
    }

    public void handle(Request request, Response response) throws Exception {
        response.statusCode(code);
        response.contentLength(contentLength());
        response.contentType("text/html; charset=" + encoding);
        response.body(page);
    }

    private int contentLength() {
        return page.getBytes().length;
    }
}
