package test.support.org.testinfected.petstore.web;

import org.simpleframework.http.Response;
import org.simpleframework.http.ResponseWrapper;

public class MockResponse extends ResponseWrapper {

    private static final Response DUMMY_RESPONSE = null;

    public MockResponse() {
        super(DUMMY_RESPONSE);
    }

    public static MockResponse aResponse() {
        return new MockResponse();
    }
}
