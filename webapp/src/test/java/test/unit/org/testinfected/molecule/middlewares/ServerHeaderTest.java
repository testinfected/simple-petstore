package test.unit.org.testinfected.molecule.middlewares;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.molecule.Application;
import org.testinfected.molecule.middlewares.ServerHeader;
import test.support.org.testinfected.molecule.unit.MockRequest;
import test.support.org.testinfected.molecule.unit.MockResponse;

import static test.support.org.testinfected.molecule.unit.MockRequest.aRequest;
import static test.support.org.testinfected.molecule.unit.MockResponse.aResponse;

@RunWith(JMock.class)
public class ServerHeaderTest {

    Mockery context = new JUnit4Mockery();
    Application successor = context.mock(Application.class, "successor");

    String serverName = "server/version";
    ServerHeader serverHeader = new ServerHeader(serverName);

    MockRequest request = aRequest();
    MockResponse response = aResponse();

    @Before public void
    chainWithSuccessor() {
        serverHeader.connectTo(successor);
    }

    @Test public void
    setsServerHeaderAndForwardsRequest() throws Exception {
        context.checking(new Expectations() {{
            oneOf(successor).handle(with(request), with(response));
        }});

        serverHeader.handle(request, response);
        response.assertHeader("Server", serverName);
    }
}