package test.support.org.testinfected.petstore.web;

import org.testinfected.petstore.MustacheRendering;
import org.testinfected.petstore.RenderingEngine;
import org.testinfected.petstore.util.Context;
import org.w3c.dom.Element;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

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
        return with(new MustacheRendering(location));
    }

    public OfflineRenderer with(RenderingEngine renderer) {
        this.renderer = renderer;
        return this;
    }

    public OfflineRenderer using(Context context) {
        return using(context.asMap());
    }

    public OfflineRenderer using(Map<String, Object> context) {
        this.context.withAll(context);
        return this;
    }

    public OfflineRenderer using(String key, Object value) {
        context.with(key, value);
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
        renderer.render(writer, template, context.asMap());
    }
}

