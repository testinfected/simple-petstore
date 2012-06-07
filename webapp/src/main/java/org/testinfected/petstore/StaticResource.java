package org.testinfected.petstore;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.Status;
import org.simpleframework.http.resource.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import static org.simpleframework.http.Status.INTERNAL_SERVER_ERROR;

public class StaticResource implements Resource {

    private final ResourceLoader resourceLoader;
    private final Renderer renderer;
    private final String charset;

    public StaticResource(ResourceLoader resourceLoader, Renderer renderer, String charset) {
        this.resourceLoader = resourceLoader;
        this.renderer = renderer;
        this.charset = charset;
    }

    public void handle(Request request, Response response) {
        renderHeaders(response);
        try {
            renderFile(request, response);
        } catch (ResourceNotFoundException e) {
            renderNotFound(e, response);
        } catch (Exception e) {
            renderInternalError(e, response);
        } finally {
            Streams.close(response);
        }
    }

    private void renderHeaders(Response response) {
        long time = System.currentTimeMillis();
        response.set("Server", "Simple/4.1.21");
        response.setDate("Date", time);
        response.setDate("Last-Modified", time);
    }

    private void renderFile(Request request, Response response) throws IOException {
        InputStream file = null;
        try {
            URL resource = resourceLoader.load(assetFile(request));
            response.set("Content-Type", MimeType.guessFrom(resource.getPath()));
            URLConnection connection = resource.openConnection();
            response.set("Content-Length", connection.getContentLength());
            file = connection.getInputStream();
            Streams.copy(file, response.getOutputStream());
        } catch (IOException e) {
            Streams.close(file);
        }
    }

    private String assetFile(Request request) {
        return "assets" + request.getPath().getPath();
    }

    private void renderNotFound(Exception error, Response response) {
        try {
            response.reset();
            response.setCode(Status.NOT_FOUND.getCode());
            response.setText(Status.NOT_FOUND.getDescription());
            response.set("Content-Type", "text/html; charset=" + charset);
            renderer.render("404", error, response);
        } catch (IOException ignored) {
        }
    }

    private void renderInternalError(Exception error, Response response) {
        try {
            response.reset();
            response.setCode(INTERNAL_SERVER_ERROR.getCode());
            response.setText(INTERNAL_SERVER_ERROR.getDescription());
            response.set("Content-Type", "text/html; charset=" + charset);
            renderer.render("500", error, response);
        } catch (IOException ignored) {
        }
    }
}
