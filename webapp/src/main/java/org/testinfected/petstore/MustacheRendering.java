package org.testinfected.petstore;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheException;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

public class MustacheRendering implements Renderer {

    private static final String TEMPLATE_EXTENSION = ".html";

    // We can't use DefaultMustacheFactory built-in resource loading, which always uses
    // the default platform encoding
    private final DefaultMustacheFactory mustacheFactory = new DefaultMustacheFactory() {
        public Reader getReader(String resourceName) {
            return read(resourceName);
        }
    };

    private final ResourceLoader resourceLoader;

    public MustacheRendering(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public String render(String template, Object context) throws IOException {
        Mustache mustache = mustacheFactory.compile(templateFile(template));
        return execute(mustache, context);
    }

    private Reader read(String resourceName) {
        Resource template = resourceLoader.load(resourceName);
        try {
            return template.read();
        } catch (IOException e) {
            throw new MustacheException("Failed to load template file: " + resourceName, e);
        }
    }

    private String execute(Mustache mustache, Object context) throws IOException {
        Writer writer = new StringWriter();
        mustache.execute(writer, context);
        return writer.toString();
    }

    private String templateFile(String templateName) {
        return templateName + TEMPLATE_EXTENSION;
    }
}
