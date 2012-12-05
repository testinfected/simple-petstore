package org.testinfected.support.middlewares;

import org.testinfected.support.*;
import org.testinfected.time.Clock;

import java.nio.charset.Charset;

public class ServerHeaders extends AbstractMiddleware {

    private final Clock clock;

    public ServerHeaders(Clock clock) {
        this.clock = clock;
    }

    public void handle(org.simpleframework.http.Request request, org.simpleframework.http.Response response) throws Exception {
        handle(new SimpleRequest(request), new SimpleResponse(response, null, Charset.defaultCharset()));
    }

    public void handle(Request request, Response response) throws Exception {
        response.header("Server", Server.NAME);
        response.headerDate("Date", clock.now().getTime());

        forward(request, response);
    }
}
