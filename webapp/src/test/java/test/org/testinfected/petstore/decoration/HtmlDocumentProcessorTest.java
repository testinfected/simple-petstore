package test.org.testinfected.petstore.decoration;

import org.junit.Test;
import org.testinfected.petstore.decoration.ContentProcessor;
import org.testinfected.petstore.decoration.HtmlDocumentProcessor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;

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
        assertThat("html chunks", processor.process(page), hasEntry("head", "\n<title>Page Title</title>\n"));
    }

    @Test public void
    extractsHtmlBody() {
        assertThat("html chunks", processor.process(page), hasEntry("body", "\n...\n"));
    }

    @Test public void
    extractsHtmlTitle() {
        assertThat("html chunks", processor.process(page), hasEntry("title", "Page Title"));
    }

}
