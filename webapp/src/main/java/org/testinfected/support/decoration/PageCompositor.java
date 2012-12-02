package org.testinfected.support.decoration;

import org.testinfected.support.View;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class PageCompositor implements Decorator {

    private final ContentProcessor processor;
    private final View<? super Map<String, Object>> view;

    public PageCompositor(ContentProcessor processor, View<? super Map<String, Object>> view) {
        this.view = view;
        this.processor = processor;
    }

    public void decorate(Writer out, String content) throws IOException {
        view.render(out, processor.process(content));
    }
}
