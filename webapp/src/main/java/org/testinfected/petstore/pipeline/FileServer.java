package org.testinfected.petstore.pipeline;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.petstore.Application;
import org.testinfected.petstore.Resource;
import org.testinfected.petstore.ResourceLoader;
import org.testinfected.petstore.ResourceNotFoundException;
import org.testinfected.petstore.util.Streams;

import java.io.IOException;
import java.io.InputStream;

public class FileServer implements Application {

    private final ResourceLoader resourceLoader;
    private final Application notFound = new NotFound();

    public FileServer(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void handle(Request request, Response response) throws Exception {
        try {
            renderFile(request, response);
        } catch (ResourceNotFoundException e) {
            renderNotFound(request, response);
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

    private void renderNotFound(Request request, Response response) throws Exception {
        notFound.handle(request, response);
    }

    private String fileName(Request request) {
        return request.getPath().getPath();
    }
}
