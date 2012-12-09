package org.testinfected.molecule.middlewares;

import org.testinfected.molecule.Application;
import org.testinfected.molecule.HttpStatus;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;
import org.testinfected.molecule.util.MimeTypes;
import org.testinfected.molecule.util.Streams;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FileServer implements Application {

    private final File root;
    private final Application notFound;

    public FileServer(File root) {
        this(root, new NotFound());
    }

    public FileServer(File root, Application notFound) {
        this.root = root;
        this.notFound = notFound;
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
        response.contentType(MimeTypes.guessFrom(file.getName()));
        response.headerDate("Last-Modified", file.lastModified());
        response.contentLength((int) file.length());

        InputStream in = new FileInputStream(file);
        try {
            Streams.copy(in, response.outputStream());
        } finally {
            Streams.close(in);
        }

        response.status(HttpStatus.OK);
    }

    private void renderNotFound(Request request, Response response) throws Exception {
        notFound.handle(request, response);
    }

    private String fileName(Request request) {
        return request.pathInfo();
    }
}
