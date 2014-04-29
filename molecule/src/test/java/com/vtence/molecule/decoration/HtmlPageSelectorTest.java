package com.vtence.molecule.decoration;

import org.junit.Test;
import com.vtence.molecule.HttpStatus;
import com.vtence.molecule.Response;

import java.io.IOException;

import static com.vtence.molecule.support.MockResponse.aResponse;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

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
