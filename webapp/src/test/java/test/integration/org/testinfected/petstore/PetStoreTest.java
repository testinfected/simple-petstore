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
import static test.support.org.testinfected.petstore.web.CharsetDetector.detectedCharset;
import static test.support.org.testinfected.petstore.web.HasHeaderWithValue.hasHeader;
import static test.support.org.testinfected.petstore.web.HasHeaderWithValue.hasNoHeader;
import static test.support.org.testinfected.petstore.web.HasStatusCode.hasStatusCode;
import static test.support.org.testinfected.petstore.web.OfflineContext.offlineContext;
import static test.support.org.testinfected.petstore.web.WebRequestBuilder.aRequest;

public class PetStoreTest {

    PetStore petStore = new PetStore(offlineContext().webRoot());

    int SERVER_LISTENS_ON = 9999;
    WebRequestBuilder request = aRequest().onPort(SERVER_LISTENS_ON).forPath("/");
    Date now = aDate().onCalendar(2012, 6, 8).atMidnight().build();

    @Before public void
    startServer() throws IOException {
        petStore.setEncoding("utf-8");
        petStore.setClock(BrokenClock.stoppedAt(now));
        petStore.start(SERVER_LISTENS_ON);
    }

    @After public void
    stopServer() throws Exception {
        petStore.stop();
    }

    @Test public void
    setsResponseHeaders() throws IOException {
        send(request);

        assertThat("response", response, hasHeader("Server", "Simple/4.1.21"));
        assertThat("response", response, hasHeader("Date", "Fri, 08 Jun 2012 04:00:00 GMT"));
    }

    @Test public void
    rendersDynamicContentAsHtmlProperlyEncoded() throws IOException {
        send(request);

        assertThat("response", response, hasStatusCode(200));
        assertThat("response", response, hasHeader("Content-Type", "text/html; charset=utf-8"));
        assertThat("detected charset", detectedCharset(toBytes(response.getContentAsStream())), containsString("UTF-8"));
        assertThat("response", response, hasNoHeader("Transfer-Encoding"));
    }

    @Test public void
    rendersStaticAssetsAsFiles() throws IOException {
        send(request.but().forPath("/images/logo.png"));

        assertThat("response", response, hasStatusCode(200));
        assertThat("response", response, hasHeader("Content-Type", "image/png"));
        assertThat("response", response, hasNoHeader("Transfer-Encoding"));
    }

    @Test public void
    render404WhenResourceIsNotFound() throws IOException {
        send(request.but().forPath("/images/missing.png"));

        assertThat("response", response, hasStatusCode(404));
        assertThat("response", response, hasNoHeader("Transfer-Encoding"));
    }


    private void send(final WebRequestBuilder request) throws IOException {
        response = client.loadWebResponse(request.build());
    }

    WebClient client = new WebClient();
    WebResponse response;
}