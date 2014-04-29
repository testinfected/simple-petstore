package com.vtence.molecule.middlewares;

import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import com.vtence.molecule.lib.AbstractMiddleware;
import com.vtence.molecule.lib.Clock;
import com.vtence.molecule.lib.SystemClock;

import static com.vtence.molecule.http.HeaderNames.DATE;

public class DateHeader extends AbstractMiddleware {

    private final Clock clock;

    public DateHeader() {
        this(new SystemClock());
    }

    public DateHeader(Clock clock) {
        this.clock = clock;
    }

    public void handle(Request request, Response response) throws Exception {
        response.set(DATE, clock.now());

        forward(request, response);
    }
}
