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
import org.testinfected.petstore.decoration.PageCompositor;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(JMock.class)
public class PageCompositorTest {

    Mockery context = new JUnit4Mockery();
    ContentProcessor contentProcessor = context.mock(ContentProcessor.class);
    Layout layout = context.mock(Layout.class);

    Decorator compositor = new PageCompositor(contentProcessor, layout);

    String originalPage = "original page";
    String decoratedPage = "decorated page";
    Map<String, String> data = new HashMap<String, String>();

    @Test public void
    processesContentAndRendersLayout() throws Exception {
        context.checking(new Expectations() {{
            oneOf(contentProcessor).process(with(originalPage)); will(returnValue(data));
            oneOf(layout).render(with(same(data))); will(returnValue(decoratedPage));
        }});

        assertThat("output", compositor.decorate(originalPage), equalTo(decoratedPage));
    }
}
