package test.support.org.testinfected.petstore.web;

import org.hamcrest.Matcher;
import com.vtence.molecule.Response;
import org.testinfected.petstore.View;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

public class MockView<T> implements View<T> {

    private Response response;
    private T context;

    public void render(Response response, T context) throws IOException {
        this.response = response;
        this.context = context;
    }

    public void assertRenderedTo(Response to) {
        assertThat("rendered to", this.response, sameInstance(to));
    }

    public void assertRenderedWith(Matcher<? super T> contextMatcher) {
        assertThat("rendering context", context, contextMatcher);
    }
}