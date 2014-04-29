package com.vtence.molecule.decoration;

import com.vtence.molecule.http.HttpStatus;
import com.vtence.molecule.support.MockResponse;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class HtmlPageSelectorTest {

    Selector selector = new HtmlPageSelector();

    MockResponse response = new MockResponse();

    @Test public void
    selectsContentOfTypeTextHtmlWhenResponseIsOk() throws IOException {
        response.contentType("text/html; charset=iso-8859-1").status(HttpStatus.OK);
        assertThat("content selection", selector.selected(response), equalTo(true));
    }

    @Test public void
    doesNotSelectContentIfNotHtml() throws IOException {
        response.contentType("text/plain").status(HttpStatus.OK);
        assertThat("content selection", selector.selected(response), equalTo(false));
    }

    @Test public void
    doesNotSelectContentWhenStatusNotOK() throws IOException {
        response.status(HttpStatus.SEE_OTHER);
        assertThat("content selection", selector.selected(response), equalTo(false));
    }

    @Test public void
    doesNotSelectResponseWithoutContentType() throws IOException {
        response.status(HttpStatus.OK);
        assertThat("content selection", selector.selected(response), equalTo(false));
    }
}
