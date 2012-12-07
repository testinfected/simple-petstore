package org.testinfected.support.middlewares;

import org.testinfected.support.Request;
import org.testinfected.support.Response;
import org.testinfected.support.util.BufferedResponse;
import org.testinfected.support.decoration.Decorator;
import org.testinfected.support.decoration.HtmlDocumentProcessor;
import org.testinfected.support.decoration.HtmlPageSelector;
import org.testinfected.support.decoration.Layout;
import org.testinfected.support.decoration.PageCompositor;
import org.testinfected.support.decoration.Selector;

import java.io.IOException;
import java.io.Writer;

public class SiteMesh extends AbstractMiddleware {

    private final Selector selector;
    private final Decorator decorator;

    public static SiteMesh html(Layout layout) {
        return new SiteMesh(new HtmlPageSelector(), new PageCompositor(new HtmlDocumentProcessor(), layout));
    }

    public SiteMesh(Selector selector, Decorator decorator) {
        this.selector = selector;
        this.decorator = decorator;
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
}
