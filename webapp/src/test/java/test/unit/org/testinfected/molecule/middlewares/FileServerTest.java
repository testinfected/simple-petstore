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

    FileServer fileServer = new FileServer(assetsFolder(), new NotFound());
    File file = new File(assetsFolder(), "images/image.png");

    MockRequest request = GET("/images/image.png");
    MockResponse response = aResponse();

    private static File assetsFolder() {
        URL folder = FileServerTest.class.getClassLoader().getResource("test/assets");
        if (folder == null) throw new AssertionError("Unable to locate resource folder");
        File assets;
        try {
            assets = new File(folder.toURI());
        } catch (Exception e) {
            throw new AssertionError(e);
        }
        return assets;
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