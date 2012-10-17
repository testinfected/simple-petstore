package org.testinfected.petstore.decoration;

import java.io.IOException;

public class PageCompositor implements Decorator {

    private final ContentProcessor processor;
    private final Layout layout;

    public PageCompositor(ContentProcessor processor, Layout layout) {
        this.layout = layout;
        this.processor = processor;
    }

    public String decorate(String content) throws IOException {
        return layout.render(processor.process(content));
    }
}
