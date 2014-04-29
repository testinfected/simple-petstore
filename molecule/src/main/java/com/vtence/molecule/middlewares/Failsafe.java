package com.vtence.molecule.middlewares;

import com.vtence.molecule.http.HttpStatus;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import com.vtence.molecule.lib.AbstractMiddleware;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class Failsafe extends AbstractMiddleware {

    public Failsafe() {}

    public void handle(Request request, Response response) throws Exception {
        try {
            forward(request, response);
        } catch (Throwable error) {
            failsafeResponse(error, response);
        }
    }

    private void failsafeResponse(Throwable error, Response response) throws IOException {
        response.status(HttpStatus.INTERNAL_SERVER_ERROR);
        response.contentType("text/html; charset=utf-8");
        response.body(formatAsHtml(error));
    }

    private String formatAsHtml(Throwable error) {
        StringWriter html = new StringWriter();
        PrintWriter buffer = new PrintWriter(html);
        buffer.println("<h1>Oups!</h1>");
        buffer.println("<h2>Sorry, an internal error occurred</h2>");
        buffer.println("<p>");
        buffer.println("  <strong>" + error.toString() + "</strong>");
        buffer.println("  <ul>");
        for (StackTraceElement stackTraceElement : error.getStackTrace()) {
            buffer.println("    <li>" + stackTraceElement.toString() + "</li>");
        }
        buffer.println("  </ul>");
        buffer.println("</p>");

        return html.toString();
    }
}
