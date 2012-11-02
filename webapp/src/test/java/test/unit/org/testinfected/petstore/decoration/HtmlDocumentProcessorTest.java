package test.unit.org.testinfected.petstore.decoration;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.testinfected.petstore.decoration.ContentProcessor;
import org.testinfected.petstore.decoration.HtmlDocumentProcessor;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;

public class HtmlDocumentProcessorTest {

    ContentProcessor processor = new HtmlDocumentProcessor();

    String page = "<html>\n" +
            "<head>\n" +
            "<title>Page Title</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "...\n" +
            "</body>\n" +
            "</html>\n";

    @Test public void
    extractsHtmlHead() {
        assertThat("html chunks", processor.process(page), hasChunk("head", "\n<title>Page Title</title>\n"));
    }

    @Test public void
    extractsHtmlBody() {
        assertThat("html chunks", processor.process(page), hasChunk("body", "\n...\n"));
    }

    @Test public void
    extractsHtmlTitle() {
        assertThat("html chunks", processor.process(page), hasChunk("title", "Page Title"));
    }

    private Matcher<Map<? extends String, ? extends Object>> hasChunk(final String key, final String value) {
        return Matchers.<String, Object>hasEntry(key, value);
    }

}
