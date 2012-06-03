package org.testinfected.petstore;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

public class MustacheRendering {

    private static final String TEMPLATE_DIRECTORY = "templates/";
    private static final String TEMPLATE_EXTENSION = ".html";

    private final String charset;
    private final ClassPathResourceLoader resourceLoader;
    private final Mustache.Compiler mustache;

    public MustacheRendering(ClassPathResourceLoader resourceLoader, String charset) {
        this.charset = charset;
        this.resourceLoader = resourceLoader;
        this.mustache = Mustache.compiler();
    }

    public String render(String templateName, Object context) throws IOException {
        StringWriter buffer = new StringWriter();
        render(templateName, context, buffer);
        return buffer.toString();
    }

    public void render(String templateName, Object context, Writer out) throws IOException {
        Reader source = resourceLoader.read(templateFile(templateName), charset);
        Template template = mustache.withLoader(new Mustache.TemplateLoader() {
            public Reader getTemplate(String name) throws Exception {
                return resourceLoader.read(templateFile(name), charset);
            }
        }).compile(source);

        template.execute(context, out);
    }

    private String templateFile(String templateName) {
        return TEMPLATE_DIRECTORY + templateName + TEMPLATE_EXTENSION;
    }
}
