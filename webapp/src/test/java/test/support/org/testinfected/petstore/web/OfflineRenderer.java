package test.support.org.testinfected.petstore.web;

import com.vtence.molecule.templating.JMustacheRenderer;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import test.support.org.testinfected.petstore.builders.Builder;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import static java.nio.charset.StandardCharsets.UTF_8;

public class OfflineRenderer {

    private final JMustacheRenderer renderer =
            new JMustacheRenderer().encoding(UTF_8).extension("html").defaultValue("");

    private final String template;
    private Object context = new Object();

    private OfflineRenderer(String template) {
        this.template = template;
    }

    public static OfflineRenderer render(String template) {
        return new OfflineRenderer(template);
    }

    public OfflineRenderer with(Builder<?> contextBuilder) {
        return with(contextBuilder.build());
    }

    public OfflineRenderer with(Object context) {
        this.context = context;
        return this;
    }

    public OfflineRenderer from(File location) {
        renderer.fromDir(location);
        return this;
    }

    public String asString() {
        try {
            StringWriter buffer = new StringWriter();
            renderer.render(buffer, template, context);
            return buffer.toString();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    public Element asDom() {
        try {
            return HTMLDocument.toElement(asString());
        } catch (IOException | SAXException e) {
            throw new AssertionError(e);
        }
    }
}