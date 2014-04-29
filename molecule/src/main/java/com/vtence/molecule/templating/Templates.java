package com.vtence.molecule.templating;

import com.vtence.molecule.Body;

import java.io.IOException;

public class Templates {
    private final RenderingEngine renderer;

    public Templates(RenderingEngine renderer) {
        this.renderer = renderer;
    }

    public Template named(final String name) {
        return new Template() {
            public Body render(Object context) throws IOException {
                return new TemplateBody(renderer, name, context);
            }
        };
    }
}
