package test.support.org.testinfected.petstore.web;

import org.testinfected.petstore.MustacheRendering;
import org.testinfected.petstore.RenderingEngine;
import org.testinfected.petstore.util.Context;
import org.w3c.dom.Element;
import test.support.com.pyxis.petstore.builders.Builder;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;

import static test.support.org.testinfected.petstore.web.HTMLDocument.toElement;

public class OfflineRenderer {

    public static OfflineRenderer render(String template) {
        return new OfflineRenderer(template);
    }

    private final String template;

    private RenderingEngine renderer;
    private Object context = Context.context();

    private OfflineRenderer(String template) {
        this.template = template;
    }

    public OfflineRenderer from(File location) {
        return using(new MustacheRendering(location));
    }

    public OfflineRenderer using(RenderingEngine renderer) {
        this.renderer = renderer;
        return this;
    }

    public OfflineRenderer withContext(Builder<?> contextBuilder) {
        return withContext(contextBuilder.build());
    }

    public OfflineRenderer withContext(Object context) {
        this.context = context;
        return this;
    }

    public OfflineRenderer with(String key, Object value) {
        ((Context) context).with(key, value);
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
        renderer.render(writer, template, context());
    }

    private Object context() {
        return context instanceof Context ? ((Context) context).asMap() : context;
    }
}

