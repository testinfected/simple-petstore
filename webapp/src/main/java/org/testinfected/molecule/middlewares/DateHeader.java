package org.testinfected.molecule.middlewares;

import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;
import org.testinfected.molecule.util.Clock;

public class DateHeader extends AbstractMiddleware {

    private final Clock clock;

    public DateHeader(Clock clock) {
        this.clock = clock;
    }

    public void handle(Request request, Response response) throws Exception {
        response.headerDate("Date", clock.now().getTime());

        forward(request, response);
    }
}
