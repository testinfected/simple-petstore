package org.testinfected.petstore.pipeline;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.Status;
import org.testinfected.petstore.FailureReporter;
import org.testinfected.petstore.Renderer;
import org.testinfected.petstore.util.Charsets;

import java.io.IOException;

public class Failsafe extends AbstractMiddleware {

    private static final String ERROR_500 = "500";

    private final Renderer renderer;

    private FailureReporter failureReporter;
    private String templateName = ERROR_500;

    public Failsafe(Renderer renderer) {
        this(renderer, FailureReporter.IGNORE);
    }

    public Failsafe(Renderer renderer, FailureReporter failureReporter) {
        this.renderer = renderer;
        this.failureReporter = failureReporter;
    }

    public void setErrorTemplate(String templateName) {
        this.templateName = templateName;
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
        setErrorStatus(response);
        resetContent(response);
        renderError(error, response);
    }

    private void resetContent(Response response) throws IOException {
        response.reset();
    }

    private void setErrorStatus(Response response) {
        response.setText(Status.INTERNAL_SERVER_ERROR.getDescription());
        response.setCode(Status.INTERNAL_SERVER_ERROR.getCode());
    }

    private void renderError(Exception error, Response response) throws IOException {
        response.set("Content-Type", "text/html; charset=" + Charsets.ISO_8859_1.name().toLowerCase());
        String body = renderer.render(templateName, error);
        byte[] bytes = body.getBytes(Charsets.ISO_8859_1);
        response.setContentLength(bytes.length);
        response.getOutputStream(bytes.length).write(bytes);
    }
}
