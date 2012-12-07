package test.integration.org.testinfected.support.middlewares;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.support.Application;
import org.testinfected.support.MiddlewareStack;
import org.testinfected.support.Request;
import org.testinfected.support.Response;
import org.testinfected.support.Server;
import org.testinfected.support.middlewares.AbstractMiddleware;
import org.testinfected.support.middlewares.ContentTypeFallback;
import test.support.org.testinfected.support.web.HttpRequest;

import java.io.IOException;

import static org.hamcrest.Matchers.equalTo;
import static test.support.org.testinfected.support.web.HttpRequest.aRequest;

@RunWith(JMock.class)
public class ContentTypeFallbackTest {
    Mockery context = new JUnit4Mockery();

    String defaultContentType = "text/html; charset=utf-8";
    String contentTypeSoFar;
    ContentTypeFallback contentTypeFallback = new ContentTypeFallback(defaultContentType);
    Application app = context.mock(Application.class);

    Server server = new Server(9999);
    HttpRequest request = aRequest().to(server);

    @Before public void
    startServer() throws IOException {
        server.run(new MiddlewareStack() {{
            use(setContentType());
            use(contentTypeFallback);
            run(app);
        }});
    }

    @After public void
    stopServer() throws Exception {
        server.shutdown();
    }

    @Test public void
    setsContentTypeHeaderInResponseIfNoneSpecified() throws Exception {
        contentTypeSoFar = null;

        context.checking(new Expectations() {{
            allowing(app).handle(with(any(Request.class)), with(any(Response.class)));
        }});

        request.send().assertHasContentType(defaultContentType);
    }

    @Test public void
    assumesDefaultContentTypeIfNoneSpecified() throws Exception {
        contentTypeSoFar = null;

        context.checking(new Expectations() {{
            oneOf(app).handle(with(any(Request.class)), with(aResponseWithContentType(defaultContentType)));
        }});

        request.send();
    }

    @Test public void
    usesExistingContentTypeIfSpecified() throws Exception {
        contentTypeSoFar = "img/png";

        context.checking(new Expectations() {{
            oneOf(app).handle(with(any(Request.class)), with(aResponseWithContentType(contentTypeSoFar)));
        }});

        request.send().assertHasContentType(contentTypeSoFar);
    }

    private Matcher<Response> aResponseWithContentType(final String contentType) {
        return new FeatureMatcher<Response, String>(equalTo(contentType), "response with content type", "content type") {
            protected String featureValueOf(Response actual) {
                return actual.contentType();
            }
        };
    }

    private SetContentType setContentType() {
        return new SetContentType();
    }

    private class SetContentType extends AbstractMiddleware {
        public void handle(Request request, Response response) throws Exception {
            response.contentType(contentTypeSoFar);
            forward(request, response);
        }
    }
}
