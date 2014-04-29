package com.vtence.molecule.middlewares;

import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import com.vtence.molecule.util.Clock;

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
