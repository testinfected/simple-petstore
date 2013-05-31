package test.unit.org.testinfected.molecule.middlewares;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import org.testinfected.molecule.Application;
import org.testinfected.molecule.HttpStatus;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;
import org.testinfected.molecule.middlewares.StaticAssets;
import test.support.org.testinfected.molecule.unit.MockRequest;
import test.support.org.testinfected.molecule.unit.MockResponse;

import static org.testinfected.molecule.HttpStatus.OK;
import static test.support.org.testinfected.molecule.unit.MockRequest.aRequest;
import static test.support.org.testinfected.molecule.unit.MockResponse.aResponse;

public class StaticAssetsTest {
    @Rule public JUnitRuleMockery context = new JUnitRuleMockery();

    Application fileServer = context.mock(Application.class, "file server");
    StaticAssets assets = new StaticAssets(fileServer, "/favicon.ico");

    MockRequest request = aRequest();
    MockResponse response = aResponse();

    @Test public void
    routesToFileServerWhenPathIsMatched() throws Exception {
        assets.serve("/static");

        context.checking(new Expectations() {{
            exactly(2).of(fileServer).handle(request, response);
        }});

        assets.handle(request.withPath("/favicon.ico"), response);
        assets.handle(request.withPath("/static"), response);
    }

    @Test public void
    forwardsWhenPathIsNotMatched() throws Exception {
        assets.connectTo(respondWith(OK));
        assets.handle(request.withPath("/home"), response);
        response.assertStatus(OK);
    }

    private Application respondWith(final HttpStatus status) {
        return new Application() {
            public void handle(Request request, Response response) throws Exception {
                response.status(status);
            }
        };
    }
}