package test.integration.org.testinfected.petstore;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testinfected.petstore.PetStore;
import org.testinfected.time.lib.BrokenClock;
import test.support.org.testinfected.petstore.web.WebRequestBuilder;

import java.io.IOException;
import java.util.Date;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testinfected.petstore.util.Streams.toBytes;
import static org.testinfected.time.lib.DateBuilder.aDate;
import static test.support.org.testinfected.petstore.web.CharsetDetector.detectCharset;
import static test.support.org.testinfected.petstore.web.HasHeaderWithValue.hasHeader;
import static test.support.org.testinfected.petstore.web.HasStatusCode.hasStatusCode;
import static test.support.org.testinfected.petstore.web.WebRequestBuilder.aRequest;

public class PetStoreTest {

    int PORT = 9999;
    PetStore petStore = new PetStore(PORT);
    Date now = aDate().onCalendar(2012, 6, 8).atMidnight().build();

    WebClient client = new WebClient();
    WebRequestBuilder request = aRequest().onPort(PORT).forPath("/");
    WebResponse response;

    @Before public void
    startServer() throws IOException {
        petStore.setEncoding("utf-8");
        petStore.setClock(BrokenClock.stoppedAt(now));
        petStore.start();
    }

    @After public void
    stopServer() throws Exception {
        petStore.stop();
    }

    @Test public void
    setsResponseHeaders() throws IOException {
        response = client.loadWebResponse(request.build());
        assertThat("response", response, hasHeader("Server", "Simple/4.1.21"));
        assertThat("response", response, hasHeader("Date", "Fri, 08 Jun 2012 04:00:00 GMT"));
    }

    @Test public void
    rendersDynamicContentAsHtmlProperlyEncoded() throws IOException {
        response = client.loadWebResponse(request.build());

        assertThat("response", response, hasStatusCode(200));
        assertThat("response", response, hasHeader("Content-Type", "text/html; charset=utf-8"));
        assertThat("detected charset", detectCharset(toBytes(response.getContentAsStream())), containsString("UTF-8"));
        assertThat("response", response, hasHeader("Transfer-Encoding", nullValue()));
    }


    @Test public void
    rendersStaticAssetsAsFiles() throws IOException {
        response = client.loadWebResponse(request.but().forPath("/images/logo.png").build());

        assertThat("response", response, hasStatusCode(200));
        assertThat("response", response, hasHeader("Content-Type", "image/png"));
        assertThat("response", response, hasHeader("Transfer-Encoding", nullValue()));
    }

    @Test public void
    render404WhenResourceIsNotFound() throws IOException {
        response = client.loadWebResponse(request.but().forPath("/images/missing.png").build());

        assertThat("response", response, hasStatusCode(404));
        assertThat("response", response, hasHeader("Transfer-Encoding", nullValue()));
    }
}