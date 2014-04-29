package test.support.org.testinfected.petstore.web;

import org.hamcrest.Matcher;
import com.vtence.molecule.Response;
import org.testinfected.petstore.Page;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

public class MockPage implements Page {

    private Response response;
    private Object context;

    public void render(Response response, Object context) throws IOException {
        this.response = response;
        this.context = context;
    }

    public void assertRenderedTo(Response to) {
        assertThat("rendered to", this.response, sameInstance(to));
    }

    public void assertRenderedWith(Matcher<Object> contextMatcher) {
        assertThat("rendering context", context, contextMatcher);
    }
}
