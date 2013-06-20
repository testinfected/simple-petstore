package org.testinfected.petstore.views;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

public class ErrorList implements Mustache.Lambda {

    private final Map<String, List<String>> errors;

    public ErrorList(Map<String, List<String>> errors) {
        this.errors = errors;
    }

    public void execute(Template.Fragment frag, Writer out) throws IOException {
        String error = trim(frag);
        if (!errors.containsKey(error)) return;
        out.write(indent(frag));
        out.write("<ol class=\"errors\">\n");
        for (String message : errors.get(error)) {
            out.write(indent(frag));
            out.write("  <li>");
            out.write(message);
            out.write("</li>\n");
        }
        out.write(indent(frag));
        out.write("</ol>\n");
    }

    private String trim(Template.Fragment frag) {
        return frag.execute().trim();
    }

    private String indent(Template.Fragment frag) {
        return frag.execute().substring(0, frag.execute().indexOf(trim(frag)));
    }
}