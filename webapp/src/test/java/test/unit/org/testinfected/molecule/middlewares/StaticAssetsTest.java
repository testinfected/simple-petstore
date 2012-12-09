package test.unit.org.testinfected.molecule.middlewares;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.molecule.Application;
import org.testinfected.molecule.middlewares.StaticAssets;
import test.support.org.testinfected.molecule.web.MockRequest;
import test.support.org.testinfected.molecule.web.MockResponse;

import static test.support.org.testinfected.molecule.web.MockRequest.aRequest;
import static test.support.org.testinfected.molecule.web.MockResponse.aResponse;

@RunWith(JMock.class)
public class StaticAssetsTest {

    Mockery context = new JUnit4Mockery();
    Application successor = context.mock(Application.class, "successor");
    Application fileServer = context.mock(Application.class, "file server");
    StaticAssets assets = new StaticAssets(fileServer, "/favicon.ico");

    MockRequest request = aRequest();
    MockResponse response = aResponse();

    @Before public void
    chainWithSuccessor() {
        assets.connectTo(successor);
    }

    @Test public void
    routesToFileServerWhenPathIsMatched() throws Exception {
        context.checking(new Expectations() {{
            oneOf(fileServer).handle(with(request), with(response));
        }});
        assets.handle(request.withPath("/favicon.ico"), response);
    }

    @Test public void
    supportsMappingMultiplePaths() throws Exception {
        context.checking(new Expectations() {{
            oneOf(fileServer).handle(with(request), with(response));
        }});
        assets.serve("/static");
        assets.handle(request.withPath("/static"), response);
    }

    @Test public void
    forwardsToNextApplicationWhenPathIsNotMatched() throws Exception {
        context.checking(new Expectations() {{
            oneOf(successor).handle(with(request), with(response));
        }});
        assets.handle(request.withPath("/home"), response);
    }
}