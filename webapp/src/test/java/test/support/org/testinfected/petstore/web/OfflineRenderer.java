package test.support.org.testinfected.petstore.web;

import com.vtence.molecule.support.TemplateRenderer;
import com.vtence.molecule.templating.JMustacheRenderer;
import org.w3c.dom.Element;
import test.support.org.testinfected.petstore.builders.Builder;

import java.io.File;
import java.io.IOException;

import static test.support.org.testinfected.petstore.web.HTMLDocument.toElement;

public class OfflineRenderer {

    private final JMustacheRenderer renderer =
            new JMustacheRenderer().encoding("utf-8").extension("html").defaultValue("");
    private final TemplateRenderer template;

    public static OfflineRenderer render(String template) {
        return new OfflineRenderer(template);
    }

    private OfflineRenderer(String template) {
        this.template = new TemplateRenderer(template);
    }

    public OfflineRenderer from(File location) {
        renderer.fromDir(location);
        return this;
    }

    public OfflineRenderer with(Builder<?> contextBuilder) {
        return with(contextBuilder.build());
    }

    public OfflineRenderer with(Object context) {
        this.template.with(context);
        return this;
    }

    public String asString() {
        try {
            return template.asString(renderer);
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    public Element asDom() {
        return toElement(asString());
    }
}