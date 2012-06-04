package org.testinfected.petstore;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.resource.Resource;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import static org.simpleframework.http.Status.INTERNAL_SERVER_ERROR;

public class Application implements Resource {

    private static final String UTF8 = "utf-8";

    private final MustacheRendering rendering;
    private final String charset;

    public Application(MustacheRendering rendering) {
        this.rendering = rendering;
        charset = UTF8;
    }

    public void handle(Request request, Response response) {
        OutputStream body = null;
        try {
            long time = System.currentTimeMillis();
            response.set("Server", "JPetStore/0.1 (Simple 4.1.21)");
            response.set("Content-Type", "text/html; charset=" + charset);
            response.setDate("Date", time);
            response.setDate("Last-Modified", time);
            body = response.getOutputStream();
            render(new OutputStreamWriter(body, response.getContentType().getCharset()));
        } catch (Exception e) {
            handleError(e, response);
        } finally {
            Streams.close(body);
        }
    }

    private void render(Writer writer) throws IOException {
        Map<String, String> data = new HashMap<String, String>();
        data.put("title", "PetStore");
        rendering.render("layout/main", data, writer);
        writer.flush();
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
}
