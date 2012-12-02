package test.unit.org.testinfected.support.decoration;

import org.hamcrest.Description;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Action;
import org.jmock.api.Invocation;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.support.decoration.ContentProcessor;
import org.testinfected.support.decoration.Decorator;
import org.testinfected.support.View;
import org.testinfected.support.decoration.PageCompositor;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(JMock.class)
public class PageCompositorTest {

    Mockery context = new JUnit4Mockery();
    ContentProcessor contentProcessor = context.mock(ContentProcessor.class);
    @SuppressWarnings("unchecked")
    View<Map<String, Object>> view = context.mock(View.class);

    Decorator compositor = new PageCompositor(contentProcessor, view);

    String originalPage = "<original page>";
    String decoratedPage = "<decorated page>";
    Map<String, Object> data = new HashMap<String, Object>();

    @Test public void
    processesContentAndRendersLayout() throws Exception {
        context.checking(new Expectations() {{
            oneOf(contentProcessor).process(with(originalPage)); will(returnValue(data));
            oneOf(view).render(with(any(Writer.class)), with(same(data))); will(write(decoratedPage));
        }});

        assertThat("decorated page", decorate(originalPage), equalTo(decoratedPage));
    }

    private Action write(String output) {
        return new Write(output);
    }

    private String decorate(final String page) throws IOException {
        StringWriter out = new StringWriter();
        compositor.decorate(out, page);
        return out.toString();
    }

    public static class Write implements Action {

        private Object content;

        public Write(Object content) {
            this.content = content;
        }

        public Object invoke(Invocation invocation) throws Throwable {
            Writer writer = (Writer) invocation.getParameter(0);
            writer.write(content.toString());
            return null;
        }

        public void describeTo(Description description) {
            description.appendText("writes to output <").appendText(content.toString()).appendText(">");
        }

    }
}
