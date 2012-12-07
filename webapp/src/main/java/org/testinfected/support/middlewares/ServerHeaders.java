package org.testinfected.support.middlewares;

import org.testinfected.support.Request;
import org.testinfected.support.Response;
import org.testinfected.support.Server;
import org.testinfected.time.Clock;

public class ServerHeaders extends AbstractMiddleware {

    private final Clock clock;

    public ServerHeaders(Clock clock) {
        this.clock = clock;
    }

    public void handle(Request request, Response response) throws Exception {
        response.header("Server", Server.NAME);
        response.headerDate("Date", clock.now().getTime());

        forward(request, response);
    }
}
