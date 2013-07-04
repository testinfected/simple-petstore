package org.testinfected.petstore.helpers;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import org.testinfected.petstore.product.AttachmentStorage;

import java.io.IOException;
import java.io.Writer;

public class PathToAttachment implements Mustache.Lambda {

    private final AttachmentStorage attachments;

    public static PathToAttachment in(AttachmentStorage storage) {
        return new PathToAttachment(storage);
    }

    public PathToAttachment(AttachmentStorage attachments) {
        this.attachments = attachments;
    }

    public AttachmentStorage attachments() {
        return attachments;
    }

    public void execute(Template.Fragment frag, Writer out) throws IOException {
        out.write(locationOf(frag.execute()));
    }

    private String locationOf(String fileName) {
        return attachments.getLocation(fileName);
    }
}
