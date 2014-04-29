package com.vtence.molecule.middlewares;

import com.vtence.molecule.Application;
import com.vtence.molecule.HttpStatus;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import com.vtence.molecule.util.MimeTypes;
import com.vtence.molecule.util.Streams;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static com.vtence.molecule.middlewares.NotFound.notFound;

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
