package org.testinfected.support.middlewares;

import org.testinfected.support.*;
import org.testinfected.support.decoration.*;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Map;

public class SiteMesh extends AbstractMiddleware {

    private final Selector selector;
    private final Decorator decorator;

    public static SiteMesh html(View<Map<String, Object>> layout) {
        return new SiteMesh(new HtmlPageSelector(), new PageCompositor(new HtmlDocumentProcessor(), layout));
    }

    public SiteMesh(Selector selector, Decorator decorator) {
        this.selector = selector;
        this.decorator = decorator;
    }

    public void handle(org.simpleframework.http.Request request, org.simpleframework.http.Response response) throws Exception {
        handle(new SimpleRequest(request), new SimpleResponse(response, null, Charset.defaultCharset()));
    }

    public void handle(Request request, Response response) throws Exception {
        BufferedResponse capture = captureResponse(request, response);
        if (isSelected(new SimpleResponse(capture, null, Charset.defaultCharset()))) {
            decorate(response, capture);
        } else {
            write(response, capture);
        }
    }

    private void decorate(Response response, BufferedResponse buffer) throws IOException {
        response.removeHeader("Content-Length");
        OutputStreamWriter out = new OutputStreamWriter(response.outputStream(), buffer.getCharset());
        decorator.decorate(out, buffer.getBody());
        out.flush();
    }

    private void write(Response response, BufferedResponse buffer) throws IOException {
        response.outputStream().write(buffer.getContent());
    }

    private BufferedResponse captureResponse(Request request, Response response) throws Exception {
        BufferedResponse buffer = new BufferedResponse(response.unwrap(org.simpleframework.http.Response.class));
        forward(request, new SimpleResponse(buffer, null, Charset.defaultCharset()));
        return buffer;
    }

    private boolean isSelected(Response response) {
        return selector.select(response);
    }
}
