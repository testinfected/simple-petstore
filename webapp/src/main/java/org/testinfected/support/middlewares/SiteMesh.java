package org.testinfected.support.middlewares;

import org.testinfected.support.*;
import org.testinfected.support.decoration.*;

import java.io.IOException;
import java.io.Writer;
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
        if (shouldDecorate(capture)) {
            decorate(response, capture);
        } else {
            write(response, capture);
        }
    }

    private void decorate(Response response, BufferedResponse buffer) throws IOException {
        response.removeHeader("Content-Length");
        Writer out = response.writer();
        decorator.decorate(out, buffer.body());
        out.flush();
    }

    private void write(Response response, BufferedResponse buffer) throws IOException {
        response.outputStream().write(buffer.content());
    }

    private BufferedResponse captureResponse(Request request, Response response) throws Exception {
        BufferedResponse capture = new BufferedResponse(response);
        forward(request, capture);
        return capture;
    }

    private boolean shouldDecorate(Response response) {
        return selector.select(response);
    }

    // todo remove eventually
    protected void forward(Request request, Response response) throws Exception {
        successor.handle(request, response);
    }
}
