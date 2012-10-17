package org.testinfected.petstore.decoration;

import org.testinfected.petstore.RenderingEngine;

import java.io.IOException;
import java.util.Map;

public class LayoutTemplate implements Layout {
    
    private final String templateName;
    private final RenderingEngine renderer;

    public LayoutTemplate(String templateName, RenderingEngine renderer) {
        this.renderer = renderer;
        this.templateName = templateName;
    }

    public String render(Map<String, String> content) throws IOException {
        return renderer.render(templateName, content);
    }
}
