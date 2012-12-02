package org.testinfected.support.middlewares;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.support.View;
import org.testinfected.support.decoration.*;
import org.testinfected.support.decoration.BufferedResponse;

import java.io.IOException;
import java.io.OutputStreamWriter;
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

    public void handle(Request request, Response response) throws Exception {
        BufferedResponse capture = captureResponse(request, response);
        if (isSelected(capture)) {
            decorate(response, capture);
        } else {
            write(response, capture);
        }
    }

    private void decorate(Response response, BufferedResponse buffer) throws IOException {
        response.remove("Content-Length");
        OutputStreamWriter out = new OutputStreamWriter(response.getOutputStream(), buffer.getCharset());
        decorator.decorate(out, buffer.getBody());
        out.flush();
    }

    private void write(Response response, BufferedResponse buffer) throws IOException {
        response.getOutputStream().write(buffer.getContent());
    }

    private BufferedResponse captureResponse(Request request, Response response) throws Exception {
        BufferedResponse buffer = new BufferedResponse(response);
        forward(request, buffer);
        return buffer;
    }

    private boolean isSelected(Response response) {
        return selector.select(response);
    }
}
