package test.unit.org.testinfected.molecule.middlewares;

import org.junit.Before;
import org.junit.Test;
import org.testinfected.molecule.HttpStatus;
import org.testinfected.molecule.middlewares.NotFound;
import test.support.org.testinfected.molecule.web.MockRequest;
import test.support.org.testinfected.molecule.web.MockResponse;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.containsString;

public class NotFoundTest {

    NotFound notFound = new NotFound();

    MockRequest request = MockRequest.aRequest().withPath("/resource");
    MockResponse response = MockResponse.aResponse();

    String content = "Not found: /resource";
    int contentLength = content.getBytes().length;

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
        response.assertContent(content);
    }

    @Test public void
    setsContentLengthHeader() throws IOException {
        response.assertHeader("Content-Length", String.valueOf(contentLength));
    }

    @Test public void
    setsContentTypeToPlainText() {
        response.assertHeader("Content-Type", containsString("text/plain"));
    }
}
