package org.testinfected.petstore.decoration;

import org.testinfected.petstore.Renderer;

import java.io.IOException;
import java.util.Map;

public class LayoutTemplate implements Layout {
    
    private final String templateName;
    private final Renderer renderer;

    public LayoutTemplate(String templateName, Renderer renderer) {
        this.renderer = renderer;
        this.templateName = templateName;
    }

    public String render(Map<String, String> content) throws IOException {
        return renderer.render(templateName, content);
    }
}
