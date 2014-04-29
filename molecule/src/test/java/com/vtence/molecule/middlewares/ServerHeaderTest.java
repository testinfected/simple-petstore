package com.vtence.molecule.middlewares;

import com.vtence.molecule.support.MockRequest;
import com.vtence.molecule.support.MockResponse;
import org.junit.Test;
import com.vtence.molecule.Application;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;

import static com.vtence.molecule.support.MockRequest.aRequest;
import static com.vtence.molecule.support.MockResponse.aResponse;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ServerHeaderTest {

    String serverName = "server/version";
    ServerHeader serverHeader = new ServerHeader(serverName);

    MockRequest request = aRequest();
    MockResponse response = aResponse();

    @Test public void
    setsServerHeader() throws Exception {
        serverHeader.connectTo(write(serverName));
        serverHeader.handle(request, response);
        assertServer(serverName);
    }

    private Application write(final String text) {
        return new Application() {
            public void handle(Request request, Response response) throws Exception {
                response.body(text);
            }
        };
    }

    private void assertServer(String server) {
        assertThat("server header", response.body(), equalTo(server));
    }
}