package com.vtence.molecule.middlewares;

import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import com.vtence.molecule.decoration.Decorator;
import com.vtence.molecule.decoration.HtmlDocumentProcessor;
import com.vtence.molecule.decoration.HtmlPageSelector;
import com.vtence.molecule.decoration.Layout;
import com.vtence.molecule.decoration.PageCompositor;
import com.vtence.molecule.decoration.Selector;
import com.vtence.molecule.util.BufferedResponse;

import java.io.BufferedWriter;
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
        Writer out = new BufferedWriter(response.writer());
        decorator.decorate(out, buffer.body());
        out.flush();
    }

    private void write(Response response, BufferedResponse buffer) throws IOException {
        response.body(buffer.body());
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
