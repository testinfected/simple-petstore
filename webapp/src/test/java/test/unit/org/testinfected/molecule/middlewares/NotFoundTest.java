package test.unit.org.testinfected.molecule.middlewares;

import org.junit.Before;
import org.junit.Test;
import org.testinfected.molecule.HttpStatus;
import org.testinfected.molecule.middlewares.NotFound;
import org.testinfected.molecule.util.Charsets;
import test.support.org.testinfected.molecule.unit.MockRequest;
import test.support.org.testinfected.molecule.unit.MockResponse;

import java.io.IOException;

import static test.support.org.testinfected.molecule.unit.MockRequest.aRequest;
import static test.support.org.testinfected.molecule.unit.MockResponse.aResponse;

public class NotFoundTest {

    NotFound notFound = new NotFound();

    MockRequest request = aRequest().withPath("/resource");
    MockResponse response = aResponse().withDefaultCharset("utf-8");

    String content = "Not found: /resource";

    @Before public void
    handleRequest() throws Exception {
        notFound.handle(request, response);
    }

    @Test public void
    setStatusCodeToNotFound() {
        response.assertStatus(HttpStatus.NOT_FOUND);
    }

    @Test public void
    rendersPageNotFound() {
        response.assertBody(content);
    }

    @Test public void
    buffersResponse() throws IOException {
        response.assertBufferSize(content.getBytes(Charsets.UTF_8).length);
    }

    @Test public void
    setsContentTypeToPlainText() {
        response.assertHeader("Content-Type", "text/plain; charset=utf-8");
    }
}
