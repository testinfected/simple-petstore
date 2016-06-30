package test.unit.org.testinfected.petstore.controllers;

import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import org.junit.Test;
import org.testinfected.petstore.controllers.StaticView;
import test.support.org.testinfected.petstore.web.MockView;

import static org.hamcrest.CoreMatchers.nullValue;

public class StaticViewTest {

    MockView<Void> page = new MockView<>();
    StaticView staticView = new StaticView(page);

    Request request = new Request();
    Response response = new Response();

    @Test public void
    rendersPageWithEmptyContext() throws Exception {
        staticView.handle(request, response);
        page.assertRenderedTo(response);
        page.assertRenderedWith(nullValue());
    }
}