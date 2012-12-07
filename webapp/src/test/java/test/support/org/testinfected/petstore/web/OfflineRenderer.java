package test.support.org.testinfected.petstore.web;

import org.testinfected.petstore.util.MustacheRendering;
import org.testinfected.petstore.RenderingEngine;
import org.testinfected.petstore.util.Context;
import org.w3c.dom.Element;
import test.support.org.testinfected.petstore.builders.Builder;

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
    private Context context = Context.context();

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

    public OfflineRenderer with(String key, Builder<?> valueBuilder) {
        return with(key, valueBuilder.build());
    }

    public OfflineRenderer with(String key, Object value) {
        context.with(key, value);
        return this;
    }

    public OfflineRenderer and(String key, Object value) {
        return with(key, value);
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
        renderer.render(writer, template, context.asMap());
    }
}



