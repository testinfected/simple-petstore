package org.testinfected.petstore.dispatch;

import org.simpleframework.http.Response;
import org.simpleframework.http.Status;
import org.testinfected.petstore.Renderer;

import java.io.IOException;
import java.nio.charset.Charset;

public class SimpleResponse implements Dispatch.Response {
    private final Response response;
    private final Renderer renderer;
    private final Charset charset;

    public SimpleResponse(Response response, Renderer renderer, Charset charset) {
        this.response = response;
        this.renderer = renderer;
        this.charset = charset;
    }

    public void render(String template, Object context) throws IOException {
        response.set("Content-Type", "text/html; charset=" + charset.name().toLowerCase());
        String body = renderer.render(template, context);
        byte[] bytes = body.getBytes(charset);
        response.setContentLength(bytes.length);
        response.getOutputStream(bytes.length).write(bytes);
    }

    public void redirectTo(String location) {
        response.setCode(Status.SEE_OTHER.getCode());
        response.setText(Status.SEE_OTHER.getDescription());
        response.set("Location", "/");
    }
}
