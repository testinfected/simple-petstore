package test.integration.org.testinfected.support.middlewares;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testinfected.support.Server;
import org.testinfected.support.middlewares.FileServer;
import org.testinfected.support.util.Streams;
import test.support.org.testinfected.support.web.HttpRequest;
import test.support.org.testinfected.support.web.HttpResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static java.lang.String.valueOf;
import static test.support.org.testinfected.support.web.HttpRequest.aRequest;

public class FileServerTest {

    File base = locateBase();
    FileServer fileServer = new FileServer(base);

    Server server = new Server(9999);
    HttpRequest request = aRequest().to(server);
    File file = new File(base, "assets/image.png");

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

    @Before public void
    startServer() throws IOException {
        server.run(fileServer);
    }

    @After public void
    stopServer() throws Exception {
        server.shutdown();
    }

    @Test public void
    rendersFile() throws Exception {
        HttpResponse response = request.get("/assets/image.png");

        response.assertOK();
        response.assertHasContentSize(file.length());
        response.assertHasContent(contentOf(file));
    }

    @Test public void
    guessesMimeTypeFromExtension() throws IOException {
        HttpResponse response = request.get("/assets/image.png");
        response.assertHasContentType("image/png");
    }

    @Test public void
    setsFileResponseHeaders() throws IOException, URISyntaxException {
        HttpResponse response = request.get("/assets/image.png");

        response.assertHasHeader("Content-Length", valueOf(file.length()));
        response.assertNotChunked();
        response.assertHasHeader("Last-Modified", modifiedDateOf(file));
    }

    @Test public void
    rendersNotFoundWhenFileIsNotFound() throws IOException {
        HttpResponse response = request.get("/images/missing");
        response.assertHasStatusCode(404);
    }

    String RFC_1123_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";

    private String modifiedDateOf(File file) {
        SimpleDateFormat httpDate = new SimpleDateFormat(RFC_1123_DATE_FORMAT);
        httpDate.setTimeZone(TimeZone.getTimeZone("GMT"));
        return httpDate.format(new Date(file.lastModified()));
    }

    private byte[] contentOf(final File file) throws IOException, URISyntaxException {
        return Streams.toBytes(new FileInputStream(file));
    }
}