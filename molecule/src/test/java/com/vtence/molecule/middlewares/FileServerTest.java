package com.vtence.molecule.middlewares;

import com.vtence.molecule.support.MockRequest;
import com.vtence.molecule.support.MockResponse;
import org.junit.Test;
import com.vtence.molecule.util.Streams;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import static com.vtence.molecule.HttpStatus.NOT_FOUND;
import static com.vtence.molecule.HttpStatus.OK;
import static com.vtence.molecule.support.MockRequest.GET;
import static com.vtence.molecule.support.MockResponse.aResponse;

public class FileServerTest {

    FileServer fileServer = new FileServer(assetsFolder());
    File file = new File(assetsFolder(), "images/image.png");

    MockRequest request = GET("/images/image.png");
    MockResponse response = aResponse();

    private static File assetsFolder() {
        URL folder = FileServerTest.class.getClassLoader().getResource("assets");
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