package test.unit.org.testinfected.molecule.middlewares;

import org.junit.Test;
import org.testinfected.molecule.Application;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;
import org.testinfected.molecule.middlewares.ServerHeader;
import test.support.org.testinfected.molecule.unit.MockRequest;
import test.support.org.testinfected.molecule.unit.MockResponse;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static test.support.org.testinfected.molecule.unit.MockRequest.aRequest;
import static test.support.org.testinfected.molecule.unit.MockResponse.aResponse;

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