package com.vtence.molecule.decoration;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;

public class HtmlDocumentProcessorTest {

    ContentProcessor processor = new HtmlDocumentProcessor();

    String page =
            "<html lang=\"en\">\n" +
            "<head lang=\"en\">\n" +
            "  <title>\n" +
            "  Page Title  \n" +
            "  </title>   \n" +
            "  <meta name=\"description\" content=\"Description\"/>\n" +
            "  <meta name=\"author\" content=\"Author\"/>\n" +
            "</head>\n" +
            "<body class=\"body\">\n" +
            "Content of the body\n" +
            "</body>\n" +
            "</html>\n";

    @Test public void
    omitsTitleWhenExtractingHtmlHead() {
        assertThat("html chunks", processor.process(page), hasChunk("head",
                        "  <meta name=\"description\" content=\"Description\"/>\n" +
                        "  <meta name=\"author\" content=\"Author\"/>"));
    }

    @Test public void
    extractsBodyFromHtmlPage() {
        assertThat("html chunks", processor.process(page), hasChunk("body", "Content of the body"));
    }

    @Test public void
    extractsHtmlTitleFromHead() {
        assertThat("html chunks", processor.process(page), hasChunk("title", "Page Title"));
    }

    @Test public void
    extractsMetaDataFromHead() {
        assertThat("html chunks", processor.process(page), hasChunk("description", "Description"));
        assertThat("html chunks", processor.process(page), hasChunk("author", "Author"));
    }

    private Matcher<Map<? extends String, ? extends String>> hasChunk(final String key,
                                                                      final String value) {
        return Matchers.hasEntry(key, value);
    }
}
