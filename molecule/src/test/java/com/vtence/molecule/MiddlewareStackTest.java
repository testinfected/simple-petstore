package com.vtence.molecule;

import com.vtence.molecule.support.MockRequest;
import com.vtence.molecule.support.MockResponse;
import org.junit.Test;
import com.vtence.molecule.middlewares.AbstractMiddleware;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

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
                response.body(order + " -> ");
                forward(request, response);
            }
        };
    }

    private Application application(final String app) {
        return new Application() {
            public void handle(Request request, Response response) throws Exception {
                response.body(app);
            }
        };
    }

    private void assertChain(String chaining) {
        assertThat("chaining", response.body(), equalTo(chaining));
    }
}
