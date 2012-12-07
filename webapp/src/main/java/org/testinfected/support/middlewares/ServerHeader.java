package org.testinfected.support.middlewares;

import org.testinfected.support.Request;
import org.testinfected.support.Response;

public class ServerHeader extends AbstractMiddleware {

    private final String serverName;

    public ServerHeader(String serverName) {
        this.serverName = serverName;
    }

    public void handle(Request request, Response response) throws Exception {
        response.header("Server", serverName);

        forward(request, response);
    }
}
