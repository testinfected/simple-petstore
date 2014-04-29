package test.unit.org.testinfected.petstore.controllers;

import com.vtence.molecule.support.MockRequest;
import com.vtence.molecule.support.MockResponse;
import org.junit.Test;
import org.testinfected.petstore.controllers.StaticView;
import test.support.org.testinfected.petstore.web.MockView;

import static org.hamcrest.CoreMatchers.nullValue;

public class StaticViewTest {

    MockView<Void> page = new MockView<Void>();
    StaticView staticView = new StaticView(page);

    MockRequest request = new MockRequest();
    MockResponse response = new MockResponse();

    @Test public void
    rendersPageWithEmptyContext() throws Exception {
        staticView.handle(request, response);
        page.assertRenderedTo(response);
        page.assertRenderedWith(nullValue());
    }
}