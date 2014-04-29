package com.vtence.molecule.middlewares;

import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import com.vtence.molecule.FailureReporter;
import com.vtence.molecule.lib.AbstractMiddleware;

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
