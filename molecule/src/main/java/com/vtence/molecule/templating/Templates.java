package com.vtence.molecule.templating;

import com.vtence.molecule.Body;

import java.io.IOException;

public class Templates {
    private final RenderingEngine renderer;

    public Templates(RenderingEngine renderer) {
        this.renderer = renderer;
    }

    public <T> Template<T> named(final String name) {
        return new Template<T>() {
            public Body render(T context) throws IOException {
                return new TemplateBody(renderer, name, context);
            }
        };
    }
}