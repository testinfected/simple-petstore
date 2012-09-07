package org.testinfected.petstore.pipeline;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.Status;
import org.testinfected.petstore.Application;
import org.testinfected.petstore.Resource;
import org.testinfected.petstore.ResourceLoader;
import org.testinfected.petstore.ResourceNotFoundException;
import org.testinfected.petstore.util.Charsets;
import org.testinfected.petstore.util.Streams;

import java.io.IOException;
import java.io.InputStream;

public class FileServer implements Application {

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
        Resource resource = resourceLoader.load(fileName(request));
        response.set("Content-Type", resource.mimeType());
        response.setDate("Last-Modified", resource.lastModified());
        response.setContentLength(resource.contentLength());
        InputStream file = resource.open();
        try {
            Streams.copy(file, response.getOutputStream(resource.contentLength()));
        } finally {
            Streams.close(file);
        }
    }

    private String fileName(Request request) {
        return request.getPath().getPath();
    }

    // todo extract to its own application and delegate
    private void renderNotFound(ResourceNotFoundException notFound, Response response) throws IOException {
        response.reset();
        response.setCode(Status.NOT_FOUND.getCode());
        response.setText(Status.NOT_FOUND.getDescription());
        String body = "Not found: " + notFound.getResource();
        byte[] bytes = body.getBytes(Charsets.ISO_8859_1);
        response.set("Content-Type", "text/plain");
        response.setContentLength(bytes.length);
        response.getOutputStream(bytes.length).write(bytes);
    }
}
