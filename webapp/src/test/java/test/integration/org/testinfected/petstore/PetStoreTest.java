package test.integration.org.testinfected.petstore;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testinfected.petstore.PetStore;
import test.support.org.testinfected.petstore.templating.WebRequestBuilder;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static test.support.org.testinfected.petstore.templating.CharsetDetector.detectCharset;
import static test.support.org.testinfected.petstore.templating.WebRequestBuilder.aRequest;

public class PetStoreTest {

    PetStore server = new PetStore(9999);
    WebClient client = new WebClient();
    WebRequestBuilder request = aRequest().onPort(9999).forPath("/");
    WebResponse response;

    @Before public void
    startServer() throws Exception {
        server.start();
    }

    @After public void
    stopServer() throws Exception {
        server.stop();
    }

    @Test public void
    setsServerResponseHeader() throws IOException {
        response = client.loadWebResponse(request.build());
        assertThat("response", response, hasHeader("Server", containsString("JPetStore")));
        response = client.loadWebResponse(request.but().forPath("/images/logo.png").build());
        assertThat("response", response, hasHeader("Server", containsString("JPetStore")));
    }

    @Test public void
    rendersPagesAsHtmlMediaTypeProperlyEncoded() throws IOException {
        server.setEncoding("WINDOWS-1252");
        response = client.loadWebResponse(request.build());
        assertThat("response", response, hasHeader("Content-Type", equalTo("text/html; charset=WINDOWS-1252")));
        assertThat("detected charset", detectCharset(response.getContentAsStream()), equalTo("WINDOWS-1252"));
    }

    @Test public void
    render404WhenResourceIsNotFound() throws IOException {
        response = client.loadWebResponse(request.but().forPath("/images/missing.img").build());
        assertThat("status code", response.getStatusCode(), equalTo(404));
        assertThat("status message", response.getStatusMessage(), equalTo("Not Found"));
        assertThat("response", response, hasHeader("Content-Type", containsString("text/html")));
    }

    private Matcher<? super WebResponse> hasHeader(final String name, final Matcher<? super String> value) {
        return new FeatureMatcher<WebResponse, String>(value, "'" + name + "' header value", "header value") {
            protected String featureValueOf(WebResponse response) {
                return response.getResponseHeaderValue(name);
            }
        };
    }
}
