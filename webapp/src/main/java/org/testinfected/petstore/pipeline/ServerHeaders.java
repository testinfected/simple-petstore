package org.testinfected.petstore.pipeline;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.petstore.Server;
import org.testinfected.time.Clock;

// Inline in ApplicationContainer since server should always include those headers?
public class ServerHeaders extends AbstractMiddleware {

    private final Clock clock;

    public ServerHeaders(Clock clock) {
        this.clock = clock;
    }

    public void handle(Request request, Response response) throws Exception {
        response.set("Server", Server.NAME);
        response.setDate("Date", clock.now().getTime());

        forward(request, response);
    }
}
