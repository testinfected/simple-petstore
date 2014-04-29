package com.vtence.molecule.decoration;

import com.vtence.molecule.Body;
import com.vtence.molecule.Request;
import com.vtence.molecule.templating.Template;

import java.io.IOException;
import java.util.Map;

public class LayoutTemplate implements Decorator {
    private final Template template;

    public LayoutTemplate(Template template) {
        this.template = template;
    }

    public Body merge(Request request, Map<String, String> content) throws IOException {
        return template.render(content);
    }
}