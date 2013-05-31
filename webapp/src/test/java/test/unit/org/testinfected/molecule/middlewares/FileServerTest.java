package test.unit.org.testinfected.molecule.middlewares;

import org.junit.Test;
import org.testinfected.molecule.middlewares.FileServer;
import org.testinfected.molecule.middlewares.NotFound;
import org.testinfected.molecule.util.Streams;
import test.support.org.testinfected.molecule.unit.MockRequest;
import test.support.org.testinfected.molecule.unit.MockResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import static org.testinfected.molecule.HttpStatus.NOT_FOUND;
import static org.testinfected.molecule.HttpStatus.OK;
import static test.support.org.testinfected.molecule.unit.MockRequest.GET;
import static test.support.org.testinfected.molecule.unit.MockResponse.aResponse;

public class FileServerTest {

    File base = locateBase();
    FileServer fileServer = new FileServer(base, new NotFound());
    File file = new File(base, "assets/image.png");

    MockRequest request = GET("assets/image.png");
    MockResponse response = aResponse();

    private static File locateBase() {
        URL fileLocation = FileServerTest.class.getClassLoader().getResource("test/assets/image.png");
        File asset;
        try {
            asset = new File(fileLocation.toURI());
        } catch (Exception e) {
            throw new AssertionError("Unable to locate test/assets/image.png");
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
        fileServer.handle(request.withPath("/images/missing.png"), response);
        response.assertStatus(NOT_FOUND);
    }

    private byte[] contentOf(final File file) throws IOException, URISyntaxException {
        return Streams.toBytes(new FileInputStream(file));
    }
}