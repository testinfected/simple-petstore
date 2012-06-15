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
import test.support.org.testinfected.petstore.web.WebRequestBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static test.support.org.testinfected.petstore.web.HasContent.hasContent;
import static test.support.org.testinfected.petstore.web.HasHeaderWithValue.hasHeader;
import static test.support.org.testinfected.petstore.web.HasStatusCode.hasStatusCode;
import static test.support.org.testinfected.petstore.web.WebRequestBuilder.aRequest;

public class FileServerTest {

    FileServer fileServer = new FileServer(new ClassPathResourceLoader());
    int PORT = 9999;
    Server server = new Server(PORT);

    WebRequestBuilder request = aRequest().onPort(PORT).forPath("/assets/image.png");
    String RFC_1123_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";

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
    rendersFile() throws Exception {
        send(request);

        assertThat("response", response, hasStatusCode(200));
        assertThat("content size", contentOf(response).length, equalTo(6597));
        assertTrue("content differs from original file", Arrays.equals(contentOf(response), contentOf(resourceFile("assets/image.png"))));
    }

    @Test public void
    guessesMimeTypeFromExtension() throws IOException {
        send(request);

        assertThat("response", response, hasHeader("Content-Type", "image/png"));
    }

    @Test public void
    setsFileResponseHeaders() throws IOException, URISyntaxException {
        send(request);

        assertThat("response", response, hasHeader("Content-Length", "6597"));
        assertThat("response", response, hasHeader("Last-Modified", modifiedDateOf(resourceFile("assets/image.png"))));
    }

    @Test public void
    renders404WhenFileIsNotFound() throws IOException {
        send(request.but().forPath("/images/missing"));

        assertThat("response", response, hasStatusCode(404));
        assertThat("response", response, hasHeader("Content-Type", "text/plain"));
        assertThat("response", response, hasContent(containsString("/images/missing")));
    }

    private String modifiedDateOf(File file) {
        SimpleDateFormat httpDate = new SimpleDateFormat(RFC_1123_DATE_FORMAT);
        httpDate.setTimeZone(TimeZone.getTimeZone("GMT"));
        return httpDate.format(new Date(file.lastModified()));
    }

    private File resourceFile(final String name) throws URISyntaxException {
        URL resource = getClass().getClassLoader().getResource(name);
        return new File(resource.toURI());
    }

    private byte[] contentOf(final File file) throws IOException, URISyntaxException {
        return Streams.toBytes(new FileInputStream(file));
    }

    private byte[] contentOf(final WebResponse response) throws IOException {
        return Streams.toBytes(response.getContentAsStream());
    }

    private void send(WebRequestBuilder request) throws IOException {
        response = client.loadWebResponse(request.build());
    }

    WebClient client = new WebClient();
    WebResponse response;
}