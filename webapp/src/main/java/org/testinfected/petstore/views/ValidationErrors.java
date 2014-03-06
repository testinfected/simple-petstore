package org.testinfected.petstore.views;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import org.testinfected.petstore.helpers.ErrorMessages;

import java.io.IOException;
import java.io.Writer;

public class ValidationErrors implements Mustache.Lambda {

    private final ErrorMessages messages;

    public ValidationErrors(ErrorMessages messages) {
        this.messages = messages;
    }

    public void execute(Template.Fragment frag, Writer out) throws IOException {
        String key = trim(frag);
        if (!messages.contains(key)) return;
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
        return messages.at(key);
    }

    private String trim(Template.Fragment frag) {
        return frag.execute().trim();
    }

    private String indent(Template.Fragment frag) {
        return frag.execute().substring(0, frag.execute().indexOf(trim(frag)));
    }

    public String toString() {
        return messages.toString();
    }
}