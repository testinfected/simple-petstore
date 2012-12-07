package org.testinfected.support.middlewares;

import org.testinfected.support.Request;
import org.testinfected.support.Response;
import org.testinfected.time.Clock;

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
