package test.support.org.testinfected.petstore.web;

import org.testinfected.petstore.RenderingEngine;
import org.testinfected.petstore.util.Context;
import org.testinfected.petstore.util.JMustacheRendering;
import org.w3c.dom.Element;
import test.support.org.testinfected.petstore.builders.Builder;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static test.support.org.testinfected.petstore.web.HTMLDocument.toElement;

public class LegacyOfflineRenderer {

    public static LegacyOfflineRenderer render(String template) {
        return new LegacyOfflineRenderer(template);
    }

    private final String template;

    private RenderingEngine renderer;
    private Context context = Context.context();

    private LegacyOfflineRenderer(String template) {
        this.template = template;
    }

    public LegacyOfflineRenderer from(File location) {
        return using(new JMustacheRendering(location));
    }

    public LegacyOfflineRenderer using(RenderingEngine renderer) {
        this.renderer = renderer;
        return this;
    }

    public LegacyOfflineRenderer with(String key, Builder<?> valueBuilder) {
        return with(key, valueBuilder.build());
    }

    public LegacyOfflineRenderer with(String key, Object value) {
        context.with(key, value);
        return this;
    }

    public LegacyOfflineRenderer and(String key, Object value) {
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
        try {
            renderer.render(writer, template, context.asMap());
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }
}
