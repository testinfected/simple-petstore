package org.testinfected.support.middlewares;

import org.testinfected.support.*;
import org.testinfected.support.util.MimeTypes;
import org.testinfected.support.util.Streams;

import java.io.*;
import java.nio.charset.Charset;

public class FileServer implements Application {

    private final Application notFound = new NotFound();
    private final File root;

    public FileServer(File root) {
        this.root = root;
    }

    public void handle(org.simpleframework.http.Request request, org.simpleframework.http.Response response) throws Exception {
        handle(new SimpleRequest(request), new SimpleResponse(response, null, Charset.defaultCharset()));
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
    }

    private void renderNotFound(Request request, Response response) throws Exception {
        notFound.handle(request.unwrap(org.simpleframework.http.Request.class), response.unwrap(org.simpleframework.http.Response.class));
    }

    private String fileName(Request request) {
        return request.pathInfo();
    }
}
