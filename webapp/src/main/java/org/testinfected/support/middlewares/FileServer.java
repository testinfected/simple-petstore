package org.testinfected.support.middlewares;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.support.Application;
import org.testinfected.support.util.MimeTypes;
import org.testinfected.support.util.Streams;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FileServer implements Application {

    private final Application notFound = new NotFound();
    private final File root;

    public FileServer(File root) {
        this.root = root;
    }

    public void handle(Request request, Response response) throws Exception {
        try {
            renderFile(request, response);
        } catch (FileNotFoundException e) {
            renderNotFound(request, response);
        }
    }

    private void renderFile(Request request, Response response) throws IOException {
        File file = new File(root, fileName(request));
        response.set("Content-Type", MimeTypes.guessFrom(file.getName()));
        response.setDate("Last-Modified", file.lastModified());
        response.setContentLength((int) file.length());

        InputStream in = new FileInputStream(file);
        try {
            Streams.copy(in, response.getOutputStream());
        } finally {
            Streams.close(in);
        }
    }

    private void renderNotFound(Request request, Response response) throws Exception {
        notFound.handle(request, response);
    }

    private String fileName(Request request) {
        return request.getPath().getPath();
    }
}
