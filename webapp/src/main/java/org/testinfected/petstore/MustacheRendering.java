package org.testinfected.petstore;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import org.testinfected.petstore.util.Charsets;
import org.testinfected.petstore.util.Streams;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

public class MustacheRendering implements Renderer {

    private static final String TEMPLATE_DIRECTORY = "templates/";
    private static final String TEMPLATE_EXTENSION = ".html";

    private final ResourceLoader resourceLoader;
    private final Mustache.Compiler mustache;

    public MustacheRendering(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        this.mustache = Mustache.compiler();
    }

    public String render(String templateName, Object context) throws IOException {
        Reader source = null;
        try {
            source = load(templateName);
            Template template = mustache.withLoader(new TemplateLoader()).compile(source);
            return template.execute(context);
        } finally {
            Streams.close(source);
        }
    }

    private Reader load(String templateName) throws IOException {
        URL template = resourceLoader.load(templateFile(templateName));
        return new InputStreamReader(template.openStream(), Charsets.UTF_8);
    }

    private String templateFile(String templateName) {
        return TEMPLATE_DIRECTORY + templateName + TEMPLATE_EXTENSION;
    }

    private class TemplateLoader implements Mustache.TemplateLoader {
        public Reader getTemplate(String name) throws Exception {
            return load(name);
        }
    }
}
