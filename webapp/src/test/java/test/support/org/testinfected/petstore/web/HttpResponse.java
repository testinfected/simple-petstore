package test.support.org.testinfected.petstore.web;

import com.gargoylesoftware.htmlunit.WebResponse;
import org.hamcrest.Matcher;
import org.junit.Assert;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testinfected.petstore.util.Streams.toBytes;
import static test.support.org.testinfected.petstore.web.CharsetDetector.detectedCharset;
import static test.support.org.testinfected.petstore.web.HasContent.hasContent;
import static test.support.org.testinfected.petstore.web.HasHeaderWithValue.hasHeader;
import static test.support.org.testinfected.petstore.web.HasHeaderWithValue.hasNoHeader;
import static test.support.org.testinfected.petstore.web.HasStatusCode.hasStatusCode;

public class HttpResponse {

    private final WebResponse response;

    public HttpResponse(WebResponse response) {
        this.response = response;
    }

    public void assertOK() {
        assertHasStatusCode(200);
    }

    public void assertNotFound() {
        assertHasStatusCode(404);
    }

    public void assertHasStatusCode(int code) {
        assertThat("response", response, hasStatusCode(code));
    }

    public void assertHasNoHeader(String name) {
        assertThat("response", response, hasNoHeader(name));
    }

    public void assertHasHeader(String name, Matcher<? super String> valueMatcher) {
        assertThat("response", response, hasHeader(name, valueMatcher));
    }

    public void assertHasHeader(String name, String value) {
        assertThat("response", response, hasHeader(name, value));
    }

    public void assertHasContent(byte[] content) throws IOException, URISyntaxException {
        Assert.assertTrue("content mismatch", Arrays.equals(content, content()));
    }

    public void assertHasContent(Matcher<? super String> contentMatcher) {
        assertThat("response", response, hasContent(contentMatcher));
    }

    public void assertContentIsEncodedAs(String charset) throws IOException {
        assertThat("response encoding", detectedCharset(content()), containsString(charset));
    }

    public void assertHasContentSize(int size) throws IOException {
        assertThat("response size", content().length, equalTo(size));
    }

    private byte[] content() throws IOException {
        return toBytes(response.getContentAsStream());
    }
}
