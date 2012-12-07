package org.testinfected.support.decoration;

import java.io.IOException;
import java.io.Writer;

public class PageCompositor implements Decorator {

    private final ContentProcessor processor;
    private final Layout layout;

    public PageCompositor(ContentProcessor processor, Layout layout) {
        this.layout = layout;
        this.processor = processor;
    }

    public void decorate(Writer out, String content) throws IOException {
        layout.render(out, processor.process(content));
    }
}
