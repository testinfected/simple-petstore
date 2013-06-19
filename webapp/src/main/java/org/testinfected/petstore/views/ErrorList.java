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
        String path = frag.execute();
        if (!errors.containsKey(path)) return;
        out.write("<ol class=\"errors\">\n");
        for (String message : errors.get(path)) {
            out.write("<li>");
            out.write(message);
            out.write("</li>");
        }
        out.write("\n</ol>");
    }
}