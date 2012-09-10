package test.integration.org.testinfected.petstore.pipeline;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testinfected.petstore.Server;
import org.testinfected.petstore.pipeline.NotFound;
import test.support.org.testinfected.petstore.web.HttpRequest;
import test.support.org.testinfected.petstore.web.HttpResponse;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.containsString;
import static test.support.org.testinfected.petstore.web.HttpRequest.aRequest;

public class NotFoundTest {

    Server server = new Server(9999);
    HttpRequest request = aRequest().to(server);

    @Before public void
    startServer() throws IOException {
        server.run(new NotFound());
    }

    @After public void
    stopServer() throws Exception {
        server.shutdown();
    }

    @Test public void
    rendersPageNotFound() throws IOException {
        HttpResponse response = request.withPath("/resource").send();
        response.assertHasStatusCode(404);
        response.assertHasHeader("Content-Type", containsString("text/plain"));
        response.assertHasContent("Not found: /resource");
        response.assertHasNoHeader("Transfer-Encoding");
    }
}
