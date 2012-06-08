package test.integration.org.testinfected.petstore.pipeline;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testinfected.petstore.ClassPathResourceLoader;
import org.testinfected.petstore.Server;
import org.testinfected.petstore.pipeline.FileServer;
import org.testinfected.petstore.util.Streams;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static test.support.org.testinfected.petstore.web.HasHeaderWithValue.hasHeader;
import static test.support.org.testinfected.petstore.web.WebRequestBuilder.aRequest;

public class FileServerTest {

    int PORT = 9999;
    Server server = new Server(PORT);

    FileServer fileServer = new FileServer(new ClassPathResourceLoader());

    WebClient client = new WebClient();
    WebResponse response;

    @Before public void
    startServer() throws IOException {
        client.setTimeout(5000);
        server.run(fileServer);
    }

    @After public void
    stopServer() throws Exception {
        server.stop();
    }

    @Test public void
    rendersFileAndGuessesMediaTypeFromExtension() throws IOException {
        response = client.loadWebResponse(aRequest().onPort(PORT).forPath("/images/github.png").build());
        assertThat("response", response, hasHeader("Content-Type", equalTo("image/png")));
        assertThat("response", response, hasHeader("Content-Length", equalTo("6597")));
        assertThat("content size", contentSize(response), equalTo(6597));
    }

    @Test public void
    renders404WhenFileIsNotFound() throws IOException {
        response = client.loadWebResponse(aRequest().onPort(PORT).forPath("/images/missing").build());
        assertThat("status code", response.getStatusCode(), equalTo(404));
        assertThat("status message", response.getStatusMessage(), equalTo("Not Found"));
        assertThat("response", response, hasHeader("Content-Type", equalTo("text/plain")));
        assertThat("content", response.getContentAsString(), containsString("/images/missing"));
    }

    private int contentSize(final WebResponse response) throws IOException {
        return Streams.toBytes(response.getContentAsStream()).length;
    }
}