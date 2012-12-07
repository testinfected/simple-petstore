package test.integration.org.testinfected.support.middlewares;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testinfected.support.MiddlewareStack;
import org.testinfected.support.Server;
import org.testinfected.support.middlewares.ContentType;
import test.support.org.testinfected.support.web.HttpRequest;

import java.io.IOException;

import static test.support.org.testinfected.support.web.HttpRequest.aRequest;
import static test.support.org.testinfected.support.web.StaticResponse.emptyResponse;

public class ContentTypeTest {

    String defaultContentType = "text/html";
    ContentType contentType = new ContentType(defaultContentType);

    Server server = new Server(9999);
    HttpRequest request = aRequest().to(server);

    @Before public void
    startServer() throws IOException {
        server.run(new MiddlewareStack() {{
            use(contentType);
            run(emptyResponse());
        }});
    }

    @After public void
    stopServer() throws Exception {
        server.shutdown();
    }

    @Test public void
    setsContentTypeHeaderWhenNoneIsSpecified() throws Exception {
        request.send().assertHasContentType(defaultContentType);
    }
}
