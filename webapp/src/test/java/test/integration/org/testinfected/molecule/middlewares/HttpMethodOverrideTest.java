package test.integration.org.testinfected.molecule.middlewares;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.molecule.Application;
import org.testinfected.molecule.HttpMethod;
import org.testinfected.molecule.MiddlewareStack;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;
import org.testinfected.molecule.Server;
import org.testinfected.molecule.middlewares.HttpMethodOverride;
import org.testinfected.molecule.simple.SimpleServer;
import test.support.org.testinfected.molecule.web.HttpRequest;

import java.io.IOException;

import static org.testinfected.molecule.HttpMethod.DELETE;
import static org.testinfected.molecule.HttpMethod.GET;
import static org.testinfected.molecule.HttpMethod.POST;
import static test.support.org.testinfected.molecule.web.HttpRequest.aRequest;

// todo Consider rewriting as unit test now that we can mock requests and responses
@RunWith(JMock.class)
public class HttpMethodOverrideTest {
    Mockery context = new JUnit4Mockery();
    Application runner = context.mock(Application.class);

    HttpMethodOverride methodOverride = new HttpMethodOverride();

    Server server = new SimpleServer(9999);
    HttpRequest request = aRequest().to(server);

    @Before public void
    startServer() throws IOException {
        server.run(new MiddlewareStack() {{
            use(methodOverride);
            run(runner);
        }});
    }

    @After public void
    stopServer() throws Exception {
        server.shutdown();
    }

    @Test public void
    doesNotAffectGetMethods() throws Exception {
        context.checking(new Expectations() {{
            oneOf(runner).handle(with(aRequestWithMethod(GET)), with(any(Response.class)));
        }});
        request.withParameter("_method", "delete").get("/");
    }

    @Test public void
    doesNotAffectPostMethodsWhenOverrideParameterIsNotSet() throws Exception {
        context.checking(new Expectations() {{
            oneOf(runner).handle(with(aRequestWithMethod(POST)), with(any(Response.class)));
        }});
        request.post("/item");
    }

    @Test public void
    changesPostMethodsAccordingToOverrideParameter() throws Exception {
        context.checking(new Expectations() {{
            oneOf(runner).handle(with(aRequestWithMethod(DELETE)), with(any(Response.class)));
        }});

        request.withParameter("_method", "delete").post("/item");
    }

    @Test public void
    doesNotChangeMethodIfOverriddenMethodIsUnknown() throws Exception {
        context.checking(new Expectations() {{
            oneOf(runner).handle(with(aRequestWithMethod(POST)), with(any(Response.class)));
        }});
        request.withParameter("_method", "foo").post("/item");
    }

    private Matcher<Request> aRequestWithMethod(HttpMethod method) {
        return new FeatureMatcher<Request, HttpMethod>(Matchers.equalTo(method), "a request with method", "method") {
            protected HttpMethod featureValueOf(Request request) {
                return request.method();
            }
        };
    }
}