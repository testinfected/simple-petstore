package org.testinfected.petstore.util;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import org.testinfected.molecule.util.Charsets;
import org.testinfected.molecule.util.Streams;
import org.testinfected.petstore.RenderingEngine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;

public class JMustacheRendering implements RenderingEngine {

    private static final String DEFAULT_TEMPLATE_EXTENSION = ".html";
    private static final Charset DEFAULT_TEMPLATE_ENCODING = Charsets.UTF_8;

    private final File base;
    private final Mustache.Compiler mustache;

    public JMustacheRendering(File base) {
        this.base = base;
        this.mustache = Mustache.compiler().defaultValue("").withLoader(new TemplateLoader());
    }

    public void render(Writer out, String view, Object context) throws IOException {
        Reader source = null;
        try {
            source = load(view);
            Template template = mustache.compile(source);
            template.execute(context, out);
        } finally {
            Streams.close(source);
        }
    }

    private Reader load(String named) throws IOException {
        return new InputStreamReader(new FileInputStream(template(named)), DEFAULT_TEMPLATE_ENCODING);
    }

    private File template(String templateName) {
        return new File(base, templateName + DEFAULT_TEMPLATE_EXTENSION);
    }

    private class TemplateLoader implements Mustache.TemplateLoader {
        public Reader getTemplate(String name) throws Exception {
            return load(name);
        }
    }
}