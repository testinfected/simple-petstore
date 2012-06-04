package org.testinfected.petstore;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.resource.Resource;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.simpleframework.http.Status.INTERNAL_SERVER_ERROR;

public class StaticAsset implements Resource {

    private final ClassPathResourceLoader resourceLoader;

    public StaticAsset(ClassPathResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void handle(Request request, Response response) {
        InputStream file = null;
        OutputStream body = null;
        try {
            file = resourceLoader.stream(assetFile(request));
            body = response.getOutputStream();
            render(file, body);
        } catch (Exception e) {
            handleError(e, response);
        } finally {
            close(file);
            close(body);
        }
    }

    private String assetFile(Request request) {
        return "assets" + request.getPath().getPath();
    }

    private void render(InputStream file, OutputStream response) throws IOException {
        file = new BufferedInputStream(file);
        response = new BufferedOutputStream(response);
        while (true) {
            int data = file.read();
            if (data == -1) break;
            response.write(data);
        }
        response.flush();
    }

    private void handleError(Exception error, Response response) {
        try {
            response.reset();
            response.setText(INTERNAL_SERVER_ERROR.getDescription());
            response.setCode(INTERNAL_SERVER_ERROR.getCode());
            PrintStream out = response.getPrintStream();
            out.println("<p>");
            out.print(error.toString());
            out.println("<br/>");
            for (StackTraceElement each : error.getStackTrace()) {
                out.print(each.toString());
                out.println("<br/>");
            }
            out.println("</p>");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void close(Closeable stream) {
        try {
            if (stream != null) stream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
