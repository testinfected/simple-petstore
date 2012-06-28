package test.integration.org.testinfected.petstore;

import com.gargoylesoftware.htmlunit.WebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testinfected.petstore.PetStore;
import org.testinfected.petstore.Server;
import test.support.org.testinfected.petstore.web.LogFile;
import test.support.org.testinfected.petstore.web.WebRequestBuilder;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testinfected.petstore.util.Streams.toBytes;
import static test.support.org.testinfected.petstore.web.CharsetDetector.detectedCharset;
import static test.support.org.testinfected.petstore.web.HasHeaderWithValue.hasHeader;
import static test.support.org.testinfected.petstore.web.HasHeaderWithValue.hasNoHeader;
import static test.support.org.testinfected.petstore.web.HasStatusCode.hasStatusCode;
import static test.support.org.testinfected.petstore.web.OfflineContext.offlineContext;
import static test.support.org.testinfected.petstore.web.WebRequestBuilder.aRequest;

public class PetStoreTest {

    PetStore petStore = new PetStore(offlineContext().webRoot());

    int SERVER_LISTENING_PORT = 9999;
    WebRequestBuilder request = aRequest().onPort(SERVER_LISTENING_PORT);
    LogFile logFile;

    @Before public void
    startServer() throws IOException {
        petStore.encodeOutputAs("utf-8");
        logFile = LogFile.create();
        petStore.logToFile(logFile.path());
        petStore.start(SERVER_LISTENING_PORT);
    }

    @After public void
    stopServer() throws Exception {
        petStore.stop();
        logFile.clear();
    }
    
    @Test public void 
    setsServerHeaders() throws IOException {
        WebResponse response = request.send();

        assertOK(response);
        assertThat("response", response, hasHeader("Server", containsString(Server.NAME)));
    }

    @Test public void
    producesAccessLog() throws IOException {
        WebResponse response = request.but().forPath("/home").send();

        assertOK(response);
        logFile.assertHasEntry(containsString("\"GET /home HTTP/1.1\" 200"));
    }

    @Test public void
    rendersDynamicContentAsHtmlProperlyEncoded() throws IOException {
        WebResponse response = request.send();

        assertOK(response);
        assertThat("response", response, hasHeader("Content-Type", "text/html; charset=utf-8"));
        assertThat("detected charset", detectedCharset(toBytes(response.getContentAsStream())), containsString("UTF-8"));
        assertThat("response", response, hasNoHeader("Transfer-Encoding"));
    }

    @Test public void
    rendersStaticAssetsAsFiles() throws IOException {
        WebResponse response = request.but().forPath("/images/logo.png").send();

        assertOK(response);
        assertThat("response", response, hasHeader("Content-Type", "image/png"));
        assertThat("response", response, hasNoHeader("Transfer-Encoding"));
    }

    @Test public void
    render404WhenResourceIsNotFound() throws IOException {
        WebResponse response = request.but().forPath("/images/missing.png").send();

        assertThat("response", response, hasStatusCode(404));
        assertThat("response", response, hasNoHeader("Transfer-Encoding"));
    }

    private void assertOK(WebResponse response) {
        assertThat("response", response, hasStatusCode(200));
    }
}