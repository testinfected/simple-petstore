package org.testinfected.molecule.middlewares;

import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;
import org.testinfected.molecule.util.FailureReporter;

public class FailureMonitor extends AbstractMiddleware {
    private final FailureReporter reporter;

    public FailureMonitor(FailureReporter reporter) {
        this.reporter = reporter;
    }

    public void handle(Request request, Response response) throws Exception {
        try {
            forward(request, response);
        } catch (Exception e) {
            reporter.errorOccurred(e);
            throw e;
        }
    }
}
