package com.vtence.molecule.middlewares;

import com.vtence.molecule.Body;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import com.vtence.molecule.decoration.ContentProcessor;
import com.vtence.molecule.decoration.Decorator;
import com.vtence.molecule.decoration.HtmlDocumentProcessor;
import com.vtence.molecule.decoration.HtmlPageSelector;
import com.vtence.molecule.decoration.LayoutTemplate;
import com.vtence.molecule.decoration.Selector;
import com.vtence.molecule.lib.AbstractMiddleware;
import com.vtence.molecule.templating.Template;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import static com.vtence.molecule.http.HeaderNames.CONTENT_LENGTH;

public class Layout extends AbstractMiddleware {

    private final Selector selector;
    private final ContentProcessor processor;
    private final Decorator decorator;

    public static Layout html(Template<Map<String, String>> layout) {
        return html(new LayoutTemplate(layout));
    }

    public static Layout html(Decorator layout) {
        return new Layout(new HtmlPageSelector(), new HtmlDocumentProcessor(), layout);
    }

    public Layout(Selector selector, ContentProcessor processor, Decorator decorator) {
        this.selector = selector;
        this.processor = processor;
        this.decorator = decorator;
    }

    public void handle(Request request, Response response) throws Exception {
        forward(request, response);

        if (selectForDecoration(response)) {
            applyDecoration(request, response);
        }
    }

    private boolean selectForDecoration(Response response) {
        return selector.selected(response);
    }

    private void applyDecoration(Request request, Response response) throws IOException {
        response.remove(CONTENT_LENGTH);
        String content = render(response.body(), response.charset());
        Map<String, String> chunks = processor.process(content);
        response.body(decorator.merge(request, chunks));
    }

    private String render(Body body, Charset charset) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        body.writeTo(buffer, charset);
        return buffer.toString(charset.name());
    }
}