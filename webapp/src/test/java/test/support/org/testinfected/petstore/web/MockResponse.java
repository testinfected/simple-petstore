package test.support.org.testinfected.petstore.web;

import org.simpleframework.http.Response;
import org.simpleframework.http.ResponseWrapper;

//todo use where possible instead of using dynamic mocks
public class MockResponse extends ResponseWrapper {

    private static final Response DUMMY_RESPONSE = null;

    public MockResponse() {
        super(DUMMY_RESPONSE);
    }
}
