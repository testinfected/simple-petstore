package org.testinfected.petstore.helpers;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

import java.io.IOException;
import java.io.Writer;

public class ErrorList implements Mustache.Lambda {

    private final FormErrors errors;

    public ErrorList(FormErrors errors) {
        this.errors = errors;
    }

    public void execute(Template.Fragment frag, Writer out) throws IOException {
        String key = trim(frag);
        if (!errors.hasError(key)) return;
        out.write(indent(frag));
        out.write("<ol class=\"errors\">\n");
        for (String message : messageFor(key)) {
            out.write(indent(frag));
            out.write("  <li>");
            out.write(message);
            out.write("</li>\n");
        }
        out.write(indent(frag));
        out.write("</ol>\n");
    }

    public Iterable<String> messageFor(String key) {
        return errors.messagesFor(key);
    }

    private String trim(Template.Fragment frag) {
        return frag.execute().trim();
    }

    private String indent(Template.Fragment frag) {
        return frag.execute().substring(0, frag.execute().indexOf(trim(frag)));
    }
}