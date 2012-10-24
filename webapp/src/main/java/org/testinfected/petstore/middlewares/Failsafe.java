package org.testinfected.petstore.middlewares;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.Status;
import org.testinfected.petstore.FailureReporter;
import org.testinfected.petstore.util.Charsets;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

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
        response.setText(Status.INTERNAL_SERVER_ERROR.getDescription());
        response.setCode(Status.INTERNAL_SERVER_ERROR.getCode());
    }

    private void renderError(Exception error, Response response) throws IOException {
        response.set("Content-Type", "text/html; charset=" + Charsets.ISO_8859_1.name().toLowerCase());
        String body = formatAsHtml(error);
        response.getPrintStream().print(body);
    }

    private String formatAsHtml(Exception error) {
        StringWriter buffer = new StringWriter();
        PrintWriter print = new PrintWriter(buffer);
        print.println("<h1>Oups!</h1>");
        print.println("<h2>Sorry, an internal error occurred</h2>");
        print.println("<p>");
        print.println("  <strong>" + error.toString() + "</strong>");
        print.println("  <ul>");
        for (StackTraceElement stackTraceElement : error.getStackTrace()) {
            print.println("    <li>" + stackTraceElement.toString() + "</li>");
        }
        print.println("  </ul>");
        print.println("</p>");
        return buffer.toString();
    }
}
