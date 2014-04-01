package org.testinfected.molecule.middlewares;

import org.testinfected.molecule.Application;
import org.testinfected.molecule.HttpStatus;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;
import org.testinfected.molecule.util.MimeTypes;
import org.testinfected.molecule.util.Streams;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static org.testinfected.molecule.middlewares.NotFound.notFound;

public class FileServer implements Application {

    private final File root;

    public FileServer(File root) {
        this.root = root;
    }

    public void handle(Request request, Response response) throws Exception {
        try {
            renderFile(request, response);
        } catch (FileNotFoundException e) {
            notFound(request, response);
        }
    }

    private void renderFile(Request request, Response response) throws IOException {
        File file = new File(root, request.pathInfo());
        response.contentType(MimeTypes.guessFrom(file.getName()));
        response.headerDate("Last-Modified", file.lastModified());
        response.contentLength(file.length());

        InputStream in = new BufferedInputStream(new FileInputStream(file));
        try {
            Streams.copy(in, response.outputStream());
        } finally {
            Streams.close(in);
        }

        response.status(HttpStatus.OK);
    }
}
