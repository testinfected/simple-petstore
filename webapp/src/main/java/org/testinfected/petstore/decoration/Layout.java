package org.testinfected.petstore.decoration;

import org.testinfected.petstore.Renderer;

import java.io.IOException;

public class Layout implements Decorator {

    private final String template;
    private final ContentProcessor processor;
    private final Renderer renderer;

    public Layout(String template, ContentProcessor processor, Renderer renderer) {
        this.template = template;
        this.processor = processor;
        this.renderer = renderer;
    }

    public String decorate(String content) throws IOException {
        return renderer.render(template, processor.process(content));
    }
}
