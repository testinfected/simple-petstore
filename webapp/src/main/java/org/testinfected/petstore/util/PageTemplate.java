package org.testinfected.petstore.util;

import com.vtence.molecule.Response;
import com.vtence.molecule.templating.RenderingEngine;
import com.vtence.molecule.templating.TemplateBody;
import org.testinfected.petstore.Page;

import java.io.IOException;

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
        response.body(new TemplateBody(renderer, template, context));
    }
}
