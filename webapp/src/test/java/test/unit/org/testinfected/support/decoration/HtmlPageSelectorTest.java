package test.unit.org.testinfected.support.decoration;

import org.junit.Test;
import org.testinfected.support.HttpStatus;
import org.testinfected.support.Response;
import org.testinfected.support.decoration.HtmlPageSelector;
import org.testinfected.support.decoration.Selector;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static test.support.org.testinfected.support.web.MockResponse.aResponse;

public class HtmlPageSelectorTest {

    Selector selector = new HtmlPageSelector();

    @Test public void
    selectsHtmlContent() throws IOException {
        Response response = aResponse().
                withContentType("text/html; charset=iso-8859-1").
                withStatus(HttpStatus.OK);
        assertThat("content selection", selector.select(response), equalTo(true));
    }

    @Test public void
    doesNotSelectContentWhenStatusNotOK() throws IOException {
        Response response = aResponse().withStatus(HttpStatus.SEE_OTHER);
        assertThat("content selection", selector.select(response), equalTo(false));
    }

    @Test public void
    doesNotSelectContentIfNotHtml() throws IOException {
        Response response = aResponse().
                withContentType("text/plain").
                withStatus(HttpStatus.OK);
        assertThat("content selection", selector.select(response), equalTo(false));
    }

    @Test public void
    doesNotSelectResponseWithoutContentType() throws IOException {
        Response response = aResponse().withStatus(HttpStatus.OK);
        assertThat("content selection", selector.select(response), equalTo(false));
    }
}
