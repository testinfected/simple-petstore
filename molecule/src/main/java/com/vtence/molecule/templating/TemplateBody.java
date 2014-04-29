package com.vtence.molecule.templating;

import com.vtence.molecule.lib.ChunkedBody;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

public class TemplateBody extends ChunkedBody {
    private final RenderingEngine renderer;
    private final Object context;
    private final String template;

    public TemplateBody(RenderingEngine renderer, String templateName, Object context) {
        this.renderer = renderer;
        this.context = context;
        this.template = templateName;
    }

    public void writeTo(OutputStream out, Charset charset) throws IOException {
        Writer writer = new BufferedWriter(new OutputStreamWriter(out, charset));
        renderer.render(writer, template, context);
        writer.flush();
    }

    public void close() throws IOException {
    }
}
