package com.vtence.molecule.middlewares;

import com.vtence.molecule.support.MockRequest;
import com.vtence.molecule.support.MockResponse;
import org.junit.Before;
import org.junit.Test;
import com.vtence.molecule.HttpStatus;
import com.vtence.molecule.util.Charsets;

import java.io.IOException;

import static com.vtence.molecule.support.MockRequest.aRequest;
import static com.vtence.molecule.support.MockResponse.aResponse;

public class NotFoundTest {

    NotFound notFound = new NotFound();

    MockRequest request = aRequest().withPath("/resource");
    MockResponse response = aResponse();

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
        response.assertBufferSize(content.getBytes(Charsets.ISO_8859_1).length);
    }

    @Test public void
    setsContentTypeToPlainText() {
        response.assertHeader("Content-Type", "text/plain");
    }
}
