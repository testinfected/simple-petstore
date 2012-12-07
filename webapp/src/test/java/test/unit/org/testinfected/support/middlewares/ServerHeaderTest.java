package test.unit.org.testinfected.support.middlewares;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.support.Application;
import org.testinfected.support.middlewares.ServerHeader;
import test.support.org.testinfected.support.web.MockRequest;
import test.support.org.testinfected.support.web.MockResponse;

@RunWith(JMock.class)
public class ServerHeaderTest {

    Mockery context = new JUnit4Mockery();

    String serverName = "server/version";
    Application successor = context.mock(Application.class);
    ServerHeader serverHeader = new ServerHeader(serverName);

    MockRequest request = MockRequest.aRequest();
    MockResponse response = MockResponse.aResponse();

    @Before public void
    chainMiddlewares() {
        serverHeader.connectTo(successor);
    }

    @Test public void
    setsServerHeaderAndForwardsRequest() throws Exception {
        context.checking(new Expectations() {{
            oneOf(successor).handle(with(request), with(response));
        }});

        serverHeader.handle(request, response);
        response.assertHasHeader("Server", serverName);
    }
}