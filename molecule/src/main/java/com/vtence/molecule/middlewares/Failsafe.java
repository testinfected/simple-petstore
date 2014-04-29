package com.vtence.molecule.middlewares;

import com.vtence.molecule.HttpStatus;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class Failsafe extends AbstractMiddleware {

    public Failsafe() {}

    public void handle(Request request, Response response) throws Exception {
        try {
            forward(request, response);
        } catch (Exception internalError) {
            failsafeResponse(internalError, response);
        }
    }

    private void failsafeResponse(Exception error, Response response) throws IOException {
        setInternalErrorStatus(response);
        resetContent(response);
        renderError(error, response);
    }

    private void resetContent(Response response) throws IOException {
        response.reset();
    }

    private void setInternalErrorStatus(Response response) {
        response.status(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void renderError(Exception error, Response response) throws IOException {
        response.contentType("text/html; charset=utf-8");
        response.body(formatAsHtml(error));
    }

    private String formatAsHtml(Exception error) {
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
