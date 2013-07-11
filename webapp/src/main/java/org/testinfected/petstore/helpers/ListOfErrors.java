package org.testinfected.petstore.helpers;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

import java.io.IOException;
import java.io.Writer;

public class ListOfErrors implements Mustache.Lambda {

    private final Form.Errors errors;

    public ListOfErrors(Form.Errors errors) {
        this.errors = errors;
    }

    public void execute(Template.Fragment frag, Writer out) throws IOException {
        String key = trim(frag);
        if (!errors.contains(key)) return;
        out.write(indent(frag));
        out.write("<ol class=\"errors\">\n");
        for (String message : errorMessages(key)) {
            out.write(indent(frag));
            out.write("  <li>");
            out.write(message);
            out.write("</li>\n");
        }
        out.write(indent(frag));
        out.write("</ol>\n");
    }

    public Iterable<String> errorMessages(String key) {
        return errors.messages(key);
    }

    private String trim(Template.Fragment frag) {
        return frag.execute().trim();
    }

    private String indent(Template.Fragment frag) {
        return frag.execute().substring(0, frag.execute().indexOf(trim(frag)));
    }

    public String toString() {
        return errors.toString();
    }
}