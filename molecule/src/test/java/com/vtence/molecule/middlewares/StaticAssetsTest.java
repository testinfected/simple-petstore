package com.vtence.molecule.middlewares;

import com.vtence.molecule.Application;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import com.vtence.molecule.support.MockRequest;
import com.vtence.molecule.support.MockResponse;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class StaticAssetsTest {

    @Rule public JUnitRuleMockery context = new JUnitRuleMockery();
    Application fileServer = new Application() {
        public void handle(Request request, Response response) throws Exception {
            response.body(request.path());
        }
    };
    StaticAssets assets = new StaticAssets(fileServer, "/favicon.ico");

    MockRequest request = new MockRequest();
    MockResponse response = new MockResponse();

    @Before public void
    setUpResponseChain() {
        assets.connectTo(new Application() {
            public void handle(Request request, Response response) throws Exception {
                response.body("Forwarded");
            }
        });
    }

    @Test public void
    servesFileWhenPathMatchesExactly() throws Exception {
        assets.handle(request.path("/favicon.ico"), response);
        response.assertBody("/favicon.ico");
    }

    @Test public void
    servesFileWhenPathMatchesUrlPrefix() throws Exception {
        assets.serve("/assets");
        assets.handle(request.path("/assets/images/logo.png"), response);
        response.assertBody("/assets/images/logo.png");
    }

    @Test public void
    servesIndexFileIfPathIndicatesADirectory() throws Exception {
        assets.serve("/faq").index("index.html");
        assets.handle(request.path("/faq/"), response);
        response.assertBody("/faq/index.html");
    }

    @Test public void
    forwardsWhenPathIsNotMatched() throws Exception {
        assets.handle(request.path("/"), response);
        response.assertBody("Forwarded");
    }
}