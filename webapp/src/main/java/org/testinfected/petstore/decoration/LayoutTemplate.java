package org.testinfected.petstore.decoration;

import org.testinfected.petstore.RenderingEngine;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class LayoutTemplate implements Layout {
    
    private final String templateName;
    private final RenderingEngine renderer;

    public LayoutTemplate(String templateName, RenderingEngine renderer) {
        this.renderer = renderer;
        this.templateName = templateName;
    }

    public void render(Writer out, Map<String, String> content) throws IOException {
        renderer.render(out, templateName, content);
    }
}
