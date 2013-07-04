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
        String path = trim(frag);
        if (!errors.inError(path)) return;
        out.write(indent(frag));
        out.write("<ol class=\"errors\">\n");
        for (String message : errors.messagesFor(path)) {
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

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ErrorList errorList = (ErrorList) o;

        if (!errors.equals(errorList.errors)) return false;

        return true;
    }

    public int hashCode() {
        return errors.hashCode();
    }

    public String toString() {
        return errors.toString();
    }
}