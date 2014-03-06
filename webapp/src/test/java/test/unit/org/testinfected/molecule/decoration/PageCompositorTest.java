package test.unit.org.testinfected.molecule.decoration;

import org.junit.Test;
import org.testinfected.molecule.decoration.ContentProcessor;
import org.testinfected.molecule.decoration.Decorator;
import org.testinfected.molecule.decoration.Layout;
import org.testinfected.molecule.decoration.PageCompositor;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class PageCompositorTest {

    Decorator compositor = new PageCompositor(new StubProcessor(), new StubLayout());

    @Test public void
    processesContentAndRendersLayout() throws Exception {
        assertThat("decorated content", decorate("content"), equalTo("<decorated>content</decorated>"));
    }

    private String decorate(final String page) throws IOException {
        StringWriter out = new StringWriter();
        compositor.decorate(out, page);
        return out.toString();
    }

    private class StubProcessor implements ContentProcessor {

        public Map<String, String> process(String content) {
            Map<String, String> fragments = new HashMap<String, String>();
            fragments.put("content", "content");
            return fragments;
        }
    }

    private class StubLayout implements Layout {

        public void render(Writer out, Map<String, String> fragments) throws IOException {
            out.write("<decorated>" + fragments.get("content") + "</decorated>");
        }
    }
}
