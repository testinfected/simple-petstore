package test.unit.org.testinfected.molecule.middlewares;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.molecule.Application;
import org.testinfected.molecule.middlewares.FileServer;
import org.testinfected.molecule.util.Streams;
import test.support.org.testinfected.molecule.web.MockRequest;
import test.support.org.testinfected.molecule.web.MockResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static java.lang.String.valueOf;
import static org.testinfected.molecule.HttpStatus.OK;
import static test.support.org.testinfected.molecule.web.MockRequest.GET;
import static test.support.org.testinfected.molecule.web.MockResponse.aResponse;

@RunWith(JMock.class)
public class FileServerTest {

    Mockery context = new JUnit4Mockery();
    Application notFound = context.mock(Application.class);

    File base = locateBase();
    FileServer fileServer = new FileServer(base, notFound);
    File file = new File(base, "assets/image.png");

    MockRequest request = GET("/assets/image.png");
    MockResponse response = aResponse();

    private static File locateBase() {
        URL fileLocation = FileServerTest.class.getClassLoader().getResource("assets/image.png");
        File asset;
        try {
            asset = new File(fileLocation.toURI());
        } catch (Exception e) {
            throw new AssertionError("Unable to locate assets/image.png");
        }
        return asset.getParentFile().getParentFile();
    }

    @Test public void
    rendersFile() throws Exception {
        fileServer.handle(request, response);

        response.assertStatus(OK);
        response.assertContentSize(file.length());
        response.assertContent(contentOf(file));
    }

    @Test public void
    guessesMimeTypeFromExtension() throws Exception {
        fileServer.handle(request, response);

        response.assertContentType("image/png");
    }

    @Test public void
    setsFileResponseHeaders() throws Exception {
        fileServer.handle(request, response);

        response.assertHeader("Content-Length", String.valueOf(file.length()));
        response.assertHeader("Last-Modified", file.lastModified());
    }

    @Test public void
    rendersNotFoundWhenFileIsNotFound() throws Exception {
        context.checking(new Expectations() {{
            oneOf(notFound).handle(with(request), with(response));
        }});

        fileServer.handle(request.withPath("/images/missing.png"), response);
    }

    private byte[] contentOf(final File file) throws IOException, URISyntaxException {
        return Streams.toBytes(new FileInputStream(file));
    }
}