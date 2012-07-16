package test.org.testinfected.petstore.decoration;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.petstore.Renderer;
import org.testinfected.petstore.decoration.ContentProcessor;
import org.testinfected.petstore.decoration.Decorator;
import org.testinfected.petstore.decoration.Layout;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(JMock.class)
public class LayoutTest {

    Mockery context = new JUnit4Mockery();
    ContentProcessor contentProcessor = context.mock(ContentProcessor.class);
    Renderer renderer = context.mock(Renderer.class);

    Decorator layout = new Layout("template", contentProcessor, renderer);

    String originalPage = "original page";
    String decoratedPage = "decorated page";
    Map<String, String> data = new HashMap<String, String>();

    @Test public void
    processesContentAndRendersLayoutTemplate() throws Exception {
        context.checking(new Expectations() {{
            oneOf(contentProcessor).process(with(originalPage)); will(returnValue(data));
            oneOf(renderer).render(with("template"), with(same(data))); will(returnValue(decoratedPage));
        }});

        assertThat("output", layout.decorate(originalPage), equalTo(decoratedPage));
    }
}
