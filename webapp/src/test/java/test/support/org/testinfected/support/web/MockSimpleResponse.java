package test.support.org.testinfected.support.web;

import org.simpleframework.http.Response;
import org.simpleframework.http.ResponseWrapper;

public class MockSimpleResponse extends ResponseWrapper {

    private static final Response DUMMY_RESPONSE = null;

    public MockSimpleResponse() {
        super(DUMMY_RESPONSE);
    }

    public static MockSimpleResponse aResponse() {
        return new MockSimpleResponse();
    }
}
