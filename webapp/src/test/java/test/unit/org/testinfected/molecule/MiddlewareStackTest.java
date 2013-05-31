package test.unit.org.testinfected.molecule;

import org.junit.Test;
import org.testinfected.molecule.Application;
import org.testinfected.molecule.Middleware;
import org.testinfected.molecule.MiddlewareStack;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;
import org.testinfected.molecule.middlewares.AbstractMiddleware;
import test.support.org.testinfected.molecule.unit.MockRequest;
import test.support.org.testinfected.molecule.unit.MockResponse;

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
