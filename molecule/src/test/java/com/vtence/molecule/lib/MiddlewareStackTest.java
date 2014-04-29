package com.vtence.molecule.lib;

import com.vtence.molecule.Application;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import com.vtence.molecule.support.MockRequest;
import com.vtence.molecule.support.MockResponse;
import org.junit.Test;

public class MiddlewareStackTest {

    MiddlewareStack stack = new MiddlewareStack();

    MockRequest request = new MockRequest();
    MockResponse response = new MockResponse();

    @Test public void
    assemblesChainInOrderOfAddition() throws Exception {
        stack.use(middleware("top"));
        stack.use(middleware("middle"));
        stack.use(middleware("bottom"));
        stack.run(application("app"));

        stack.handle(request, response);
        assertChain("top -> middle -> bottom -> app");
    }

    private Middleware middleware(final String order) {
        return new AbstractMiddleware() {
            public void handle(Request request, Response response) throws Exception {
                forward(request, response);
                response.set("chain", order + " -> " + response.get("chain"));
            }
        };
    }

    private Application application(final String app) {
        return new Application() {
            public void handle(Request request, Response response) throws Exception {
                response.set("chain", app);
            }
        };
    }

    private void assertChain(String chaining) {
        response.assertHeader("chain", chaining);
    }
}
