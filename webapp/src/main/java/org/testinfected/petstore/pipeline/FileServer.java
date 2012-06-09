package org.testinfected.petstore.pipeline;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.Status;
import org.testinfected.petstore.Handler;
import org.testinfected.petstore.ResourceLoader;
import org.testinfected.petstore.ResourceNotFoundException;
import org.testinfected.petstore.util.Charsets;
import org.testinfected.petstore.util.MimeTypes;
import org.testinfected.petstore.util.Streams;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class FileServer implements Handler {

    private static final String ROOT = "assets";

    private final ResourceLoader resourceLoader;

    public FileServer(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void handle(Request request, Response response) throws Exception {
        try {
            renderFile(request, response);
        } catch (ResourceNotFoundException e) {
            renderNotFound(e, response);
        }
    }

    private void renderFile(Request request, Response response) throws IOException {
        URL resource = resourceLoader.load(assetFile(request));
        response.set("Content-Type", MimeTypes.guessFrom(resource.getPath()));
        URLConnection connection = resource.openConnection();
        response.setDate("Last-Modified", connection.getLastModified());
        InputStream file = connection.getInputStream();
        try {
            Streams.copy(file, response.getOutputStream(connection.getContentLength()));
        } finally {
            Streams.close(file);
        }
    }

    private String assetFile(Request request) {
        return ROOT + request.getPath().getPath();
    }

    private void renderNotFound(ResourceNotFoundException notFound, Response response) throws IOException {
        response.reset();
        response.setCode(Status.NOT_FOUND.getCode());
        response.setText(Status.NOT_FOUND.getDescription());
        String body = "File not found: " + notFound.getResource();
        byte[] bytes = body.getBytes(Charsets.ISO_8859_1);
        response.set("Content-Type", "text/plain");
        response.getOutputStream(bytes.length).write(bytes);
    }
}
