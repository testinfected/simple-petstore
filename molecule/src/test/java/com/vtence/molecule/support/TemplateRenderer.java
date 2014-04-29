package com.vtence.molecule.support;

import com.vtence.molecule.templating.RenderingEngine;

import java.io.IOException;
import java.io.StringWriter;

public class TemplateRenderer {

    private final String template;
    private Object context = new Object();

    public static TemplateRenderer render(String template) {
        return new TemplateRenderer(template);
    }

    public TemplateRenderer(String template) {
        this.template = template;
    }

    public TemplateRenderer with(Object context) {
        this.context = context;
        return this;
    }

    public String asString(RenderingEngine renderer) throws IOException {
        StringWriter buffer = new StringWriter();
        renderer.render(buffer, template, context);
        return buffer.toString();
    }
}
