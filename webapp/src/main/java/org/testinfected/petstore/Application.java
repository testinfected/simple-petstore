package org.testinfected.petstore;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.resource.Resource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import static org.simpleframework.http.Status.INTERNAL_SERVER_ERROR;

public class Application implements Resource {

    private static final int BUFFER_SIZE = 8192;
    private static final String UTF8 = "utf-8";

    public void handle(Request request, Response response) {
        try {
            long time = System.currentTimeMillis();
            response.set("Server", "JPetStore/0.1 (Simple 4.1.21)");
            response.set("Content-Type", "text/html; charset=" + UTF8);
            response.setDate("Date", time);
            response.setDate("Last-Modified", time);
            render(response);
        } catch (Exception e) {
            handleError(e, response);
        } finally {
            close(response);
        }
    }

    private void render(Response response) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Reader source = new InputStreamReader(classLoader.getResourceAsStream("templates/layout/main.html"), UTF8);
        Template template = Mustache.compiler().compile(source);
        Map<String, String> data = new HashMap<String, String>();
        data.put("title", "PetStore");
        OutputStream out = response.getOutputStream(BUFFER_SIZE);
        OutputStreamWriter writer = new OutputStreamWriter(out, response.getContentType().getCharset());
        template.execute(data, writer);
        writer.close();
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

    private void close(Response response) {
        try {
            response.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
