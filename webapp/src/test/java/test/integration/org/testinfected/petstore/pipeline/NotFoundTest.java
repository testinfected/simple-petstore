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
    HttpRequest request = aRequest().withPath("/resource").to(server);
    HttpResponse response;
    String content = "Not found: /resource";
    int contentLength = content.getBytes().length;

    @Before public void
    sendRequestToServer() throws IOException {
        server.run(new NotFound());
        response = request.send();
    }

    @After public void
    stopServer() throws Exception {
        server.shutdown();
    }

    @Test public void
    setStatusCodeTo404() {
        response.assertHasStatusCode(404);
    }

    @Test public void
    setStatusMessageToNotFound() {
        response.assertHasStatusMessage("Not Found");
    }

    @Test public void
    rendersPageNotFound() {
        response.assertHasContent(content);
    }

    @Test public void
    setsContentLengthHeader() throws IOException {
        response.assertHasHeader("Content-Length", String.valueOf(contentLength));
    }

    @Test public void
    setsResponseContentTypeToPlainText() {
        response.assertHasHeader("Content-Type", containsString("text/plain"));
    }

    @Test public void
    ensuresResponseIsNotChunked() {
        response.assertHasNoHeader("Transfer-Encoding");
    }
}
