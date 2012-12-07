package org.testinfected.support;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;

import java.io.IOException;
import java.nio.charset.Charset;

public class ApplicationContainer implements Container {

    private final Application app;
    private final FailureReporter failureReporter;
    private final Charset defaultCharset;

    public ApplicationContainer(Application app, FailureReporter failureReporter, Charset defaultCharset) {
        this.app = app;
        this.failureReporter = failureReporter;
        this.defaultCharset = defaultCharset;
    }

    public void handle(Request request, Response response) {
        try {
            run(request, response);
        } catch (Exception failure) {
            failureReporter.internalErrorOccurred(failure);
        } finally {
            close(response);
        }
    }

    private void run(Request request, Response response) throws Exception {
        app.handle(new SimpleRequest(request), new SimpleResponse(response, null, defaultCharset));
    }

    private void close(Response response) {
        try {
            response.close();
        } catch (IOException failure) {
            failureReporter.communicationFailed(failure);
        }
    }
}
