package com.vtence.molecule.middlewares;

import com.vtence.molecule.support.MockRequest;
import com.vtence.molecule.support.MockResponse;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import com.vtence.molecule.Application;
import com.vtence.molecule.HttpStatus;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;

import static com.vtence.molecule.HttpStatus.OK;
import static com.vtence.molecule.support.MockRequest.aRequest;
import static com.vtence.molecule.support.MockResponse.aResponse;

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