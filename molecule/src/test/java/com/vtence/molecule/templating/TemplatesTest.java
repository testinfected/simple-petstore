package com.vtence.molecule.templating;

import com.vtence.molecule.support.MockResponse;
import org.junit.Test;

import java.io.IOException;

import static com.vtence.molecule.support.ResourceLocator.locateOnClasspath;
import static org.hamcrest.Matchers.containsString;

public class TemplatesTest {
    RenderingEngine renderer = new JMustacheRenderer().fromDir(locateOnClasspath("views"));
    Templates templates = new Templates(renderer);

    Template<Context> template = templates.named("hello");
    MockResponse response = new MockResponse();

    @Test public void
    rendersTemplateUsingProvidedContext() throws IOException {
        response.body(template.render(new Context()));
        response.assertBody(containsString("Hello World"));
    }

    public static class Context {
        String name = "World";
    }
}
