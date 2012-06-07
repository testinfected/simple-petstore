package org.testinfected.petstore;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import org.simpleframework.http.Response;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;

public class MustacheRendering implements Renderer {

    private static final String TEMPLATE_DIRECTORY = "templates/";
    private static final String TEMPLATE_EXTENSION = ".html";

    private final String charset;
    private final ResourceLoader resourceLoader;
    private final Mustache.Compiler mustache;

    public MustacheRendering(ResourceLoader resourceLoader, String charset) {
        this.charset = charset;
        this.resourceLoader = resourceLoader;
        this.mustache = Mustache.compiler();
    }

    public String render(String templateName, Object context) throws IOException {
        StringWriter buffer = new StringWriter();
        render(templateName, context, buffer);
        return buffer.toString();
    }

    public void render(String name, Object context, Response response) throws IOException {
        Writer body = new OutputStreamWriter(response.getOutputStream(), response.getContentType().getCharset());
        render(name, context, body);
        body.flush();
    }

    public void render(String templateName, Object context, Writer out) throws IOException {
        Reader source = read(templateName);
        try {
            Template template = mustache.withLoader(new TemplateLoader()).compile(source);
            template.execute(context, out);
        } finally {
            Streams.close(source);
        }
    }

    private Reader read(String templateName) throws IOException {
        URL template = resourceLoader.load(templateFile(templateName));
        return new InputStreamReader(template.openStream(), charset);
    }

    private String templateFile(String templateName) {
        return TEMPLATE_DIRECTORY + templateName + TEMPLATE_EXTENSION;
    }

    private class TemplateLoader implements Mustache.TemplateLoader {
        public Reader getTemplate(String name) throws Exception {
            return read(name);
        }
    }
}
