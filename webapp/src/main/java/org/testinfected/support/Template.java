package org.testinfected.support;

import java.io.IOException;
import java.io.Writer;

public class Template<T> implements View<T> {
    
    private final String name;
    private final RenderingEngine renderer;

    public Template(String name, RenderingEngine renderer) {
        this.name = name;
        this.renderer = renderer;
    }

    public void render(Writer out, T context) throws IOException {
        renderer.render(out, name, context);
    }
}
