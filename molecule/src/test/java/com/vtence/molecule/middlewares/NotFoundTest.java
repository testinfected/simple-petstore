package com.vtence.molecule.middlewares;

import com.vtence.molecule.http.HttpStatus;
import com.vtence.molecule.support.MockRequest;
import com.vtence.molecule.support.MockResponse;
import org.junit.Before;
import org.junit.Test;

public class NotFoundTest {

    NotFound notFound = new NotFound();

    MockRequest request = new MockRequest();
    MockResponse response = new MockResponse();

    String content = "Not found: /resource";

    @Before public void
    handleRequest() throws Exception {
        notFound.handle(request.path("/resource"), response);
    }

    @Test public void
    setsStatusCodeToNotFound() {
        response.assertStatus(HttpStatus.NOT_FOUND);
    }

    @Test public void
    rendersPageNotFound() {
        response.assertBody(content);
    }

    @Test public void
    setsContentTypeToPlainText() {
        response.assertHeader("Content-Type", "text/plain");
    }
}