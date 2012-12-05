package org.testinfected.support.middlewares;

import org.testinfected.support.*;
import org.testinfected.support.util.Charsets;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;

public class Failsafe extends AbstractMiddleware {

    private FailureReporter failureReporter;

    public Failsafe() {
        this(FailureReporter.IGNORE);
    }

    public Failsafe(FailureReporter failureReporter) {
        this.failureReporter = failureReporter;
    }

    public void reportErrorsTo(FailureReporter failureReporter) {
        this.failureReporter = failureReporter;
    }

    public void handle(org.simpleframework.http.Request request, org.simpleframework.http.Response response) throws Exception {
        handle(new SimpleRequest(request), new SimpleResponse(response, null, Charset.defaultCharset()));
    }

    public void handle(Request request, Response response) throws Exception {
        try {
            forward(request, response);
        } catch (Exception internalError) {
            reportInternalError(internalError);
            failsafeResponse(internalError, response);
        }
    }

    private void reportInternalError(Exception error) {
        failureReporter.internalErrorOccurred(error);
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
        response.contentType("text/html; charset=" + Charsets.ISO_8859_1.name().toLowerCase());
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
