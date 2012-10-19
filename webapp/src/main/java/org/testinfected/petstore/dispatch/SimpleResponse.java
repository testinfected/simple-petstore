package org.testinfected.petstore.dispatch;

import org.simpleframework.http.Response;
import org.simpleframework.http.Status;
import org.testinfected.petstore.RenderingEngine;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

public class SimpleResponse implements Dispatch.Response {
    private final Response response;
    private final RenderingEngine renderer;
    private final Charset charset;

    public SimpleResponse(Response response, RenderingEngine renderer, Charset charset) {
        this.response = response;
        this.renderer = renderer;
        this.charset = charset;
    }

    public void render(String view, Object context) throws IOException {
        response.set("Content-Type", "text/html; charset=" + charset.name().toLowerCase());
        OutputStreamWriter out = new OutputStreamWriter(response.getOutputStream(), charset);
        renderer.render(out, view, context);
        out.flush();
    }

    public void redirectTo(String location) {
        response.setCode(Status.SEE_OTHER.getCode());
        response.setText(Status.SEE_OTHER.getDescription());
        response.set("Location", location);
    }
}
