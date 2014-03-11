package test.support.org.testinfected.petstore.web;

import org.testinfected.petstore.RenderingEngine;
import org.testinfected.petstore.util.JMustacheRendering;
import org.w3c.dom.Element;
import test.support.org.testinfected.petstore.builders.Builder;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static test.support.org.testinfected.petstore.web.HTMLDocument.toElement;

public class OfflineRenderer {

    public static OfflineRenderer render(String template) {
        return new OfflineRenderer(template);
    }

    private final String template;

    private RenderingEngine renderer;
    private Object context;

    private OfflineRenderer(String template) {
        this.template = template;
    }

    public OfflineRenderer from(File location) {
        return using(new JMustacheRendering(location));
    }

    public OfflineRenderer using(RenderingEngine renderer) {
        this.renderer = renderer;
        return this;
    }

    public OfflineRenderer with(Builder<?> contextBuilder) {
        return with(contextBuilder.build());
    }

    public OfflineRenderer with(Object context) {
        this.context = context;
        return this;
    }

    public String asString() {
        StringWriter buffer = new StringWriter();
        render(buffer);
        return buffer.toString();
    }

    public Element asDom() {
        return toElement(asString());
    }

    private void render(final Writer writer) {
        try {
            renderer.render(writer, template, context);
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }
}