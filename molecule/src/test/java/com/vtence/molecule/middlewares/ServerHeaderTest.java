package com.vtence.molecule.middlewares;

import com.vtence.molecule.Application;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import com.vtence.molecule.support.MockRequest;
import com.vtence.molecule.support.MockResponse;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ServerHeaderTest {

    String serverName = "server/version";
    ServerHeader serverHeader = new ServerHeader(serverName);

    MockRequest request = new MockRequest();
    MockResponse response = new MockResponse();

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
        assertThat("server header", response.text(), equalTo(server));
    }
}