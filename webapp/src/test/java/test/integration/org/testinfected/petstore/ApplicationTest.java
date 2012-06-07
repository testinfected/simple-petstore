package test.integration.org.testinfected.petstore;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.petstore.Application;
import org.testinfected.petstore.Charsets;
import org.testinfected.petstore.ResourceLoader;
import org.testinfected.petstore.Streams;
import test.support.org.testinfected.petstore.templating.WebRequestBuilder;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static test.support.org.testinfected.petstore.templating.CharsetDetector.detectCharset;
import static test.support.org.testinfected.petstore.templating.WebRequestBuilder.aRequest;

@RunWith(JMock.class)
public class ApplicationTest {

    Mockery context = new JUnit4Mockery();
    ResourceLoader resourceLoader = context.mock(ResourceLoader.class);

    int PORT = 9999;
    Charset charset = Charsets.UTF_16;

    Application server;
    WebClient client = new WebClient();
    WebRequestBuilder request = aRequest().onPort(PORT).forPath("/");
    WebResponse response;

    @After public void
    stopServer() throws Exception {
        server.stop();
    }

    @Test public void
    setsServerResponseHeader() throws IOException {
        startServer();

        response = client.loadWebResponse(request.build());
        assertThat("response", response, hasHeader("Server", equalTo("Simple/4.1.21")));
        response = client.loadWebResponse(request.but().forPath("/images/logo.png").build());
        assertThat("response", response, hasHeader("Server", equalTo("Simple/4.1.21")));
    }

    @Test public void
    rendersPagesAsHtmlMediaTypeProperlyEncoded() throws IOException {
        startServer();

        response = client.loadWebResponse(request.build());
        assertThat("response", response, hasHeader("Content-Type", equalTo("text/html; charset=UTF-16")));
        assertThat("detected charset", detectCharset(response.getContentAsStream()), containsString("UTF-16"));
    }

    @Test public void
    rendersAssetsGuessingMediaTypeFromExtension() throws IOException {
        startServer();

        response = client.loadWebResponse(request.but().forPath("/images/github.png").build());
        assertThat("response", response, hasHeader("Content-Type", equalTo("image/png")));
        assertThat("response", response, hasHeader("Content-Length", equalTo("6597")));
        byte[] content = Streams.toByteArray(response.getContentAsStream());
        assertThat("content size", content.length, equalTo(6597));
    }

    @Test public void
    render404WhenResourceIsNotFound() throws IOException {
        startServer();

        response = client.loadWebResponse(request.but().forPath("/images/missing.img").build());
        assertThat("status code", response.getStatusCode(), equalTo(404));
        assertThat("status message", response.getStatusMessage(), equalTo("Not Found"));
        assertThat("response", response, hasHeader("Content-Type", containsString("text/html")));
    }

    @Test public void
    render500WhenInternalErrorOccurs() throws IOException {
        crashServer();

        response = client.loadWebResponse(request.build());
        assertThat("status code", response.getStatusCode(), equalTo(500));
        assertThat("status message", response.getStatusMessage(), equalTo("Internal Server Error"));
        assertThat("response", response, hasHeader("Content-Type", containsString("text/html")));
    }

    private void startServer() throws IOException {
        server = new Application(charset);
        server.start(PORT);
    }

    private void crashServer() throws IOException {
        server = new Application(resourceLoader);
        context.checking(new Expectations() {{
            allowing(resourceLoader);
            will(throwException(new Exception()));
        }});
        server.start(PORT);
    }

    private Matcher<? super WebResponse> hasHeader(final String name, final Matcher<? super String> value) {
        return new FeatureMatcher<WebResponse, String>(value, "'" + name + "' header value", "header value") {
            protected String featureValueOf(WebResponse actual) {
                return actual.getResponseHeaderValue(name);
            }
        };
    }
}
