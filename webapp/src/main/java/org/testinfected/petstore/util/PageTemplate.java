package org.testinfected.petstore.util;

import org.testinfected.molecule.Response;
import org.testinfected.petstore.Page;
import org.testinfected.petstore.RenderingEngine;

import java.io.IOException;
import java.io.Writer;

public class PageTemplate implements Page {

    private final RenderingEngine renderer;
    private final String template;
    private final String mediaType;

    public PageTemplate(RenderingEngine renderer, String template, String mediaType) {
        this.renderer = renderer;
        this.template = template;
        this.mediaType = mediaType;
    }

    public void render(Response response, Object context) throws IOException {
        response.contentType(mediaType);
        Writer out = response.writer();
        renderer.render(out, template, context);
        out.flush();
    }
}
