package org.testinfected.petstore.util;

import org.testinfected.petstore.Page;
import org.testinfected.petstore.RenderingEngine;
import org.testinfected.molecule.Response;
import org.testinfected.molecule.util.MimeTypes;

import java.io.IOException;
import java.io.Writer;

public class PageTemplate implements Page {

    private final RenderingEngine renderer;
    private final String template;
    private final String mediaType;

    public static PageTemplate html(RenderingEngine renderer, String template) {
        return new PageTemplate(renderer, template, MimeTypes.TEXT_HTML);
    }

    public PageTemplate(RenderingEngine renderer, String template, String mediaType) {
        this.renderer = renderer;
        this.template = template;
        this.mediaType = mediaType;
    }

    public void render(Response response, Object context) throws IOException {
        response.contentType(mediaType + "; charset=" + response.charset().name().toLowerCase());
        Writer out = response.writer();
        renderer.render(out, template, context);
        out.flush();
    }
}
