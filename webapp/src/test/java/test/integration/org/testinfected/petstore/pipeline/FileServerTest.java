package test.integration.org.testinfected.petstore.pipeline;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testinfected.petstore.ClassPathResourceLoader;
import org.testinfected.petstore.Server;
import org.testinfected.petstore.pipeline.FileServer;
import org.testinfected.petstore.util.Streams;
import test.support.org.testinfected.petstore.web.HttpRequest;
import test.support.org.testinfected.petstore.web.HttpResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static test.support.org.testinfected.petstore.web.HttpRequest.aRequest;

public class FileServerTest {

    FileServer fileServer = new FileServer(new ClassPathResourceLoader());

    Server server = new Server(9999);
    HttpRequest request = aRequest().to(server);

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
        response.assertHasContentSize(6597);
        response.assertHasContent(contentOf(resourceFile("assets/image.png")));
    }

    @Test public void
    guessesMimeTypeFromExtension() throws IOException {
        HttpResponse response = request.get("/assets/image.png");
        response.assertHasHeader("Content-Type", "image/png");
    }

    @Test public void
    setsFileResponseHeaders() throws IOException, URISyntaxException {
        HttpResponse response = request.get("/assets/image.png");

        response.assertHasHeader("Content-Length", "6597");
        response.assertHasNoHeader("Transfer-Encoding");
        response.assertHasHeader("Last-Modified", modifiedDateOf(resourceFile("assets/image.png")));
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

    private File resourceFile(final String name) throws URISyntaxException {
        URL resource = getClass().getClassLoader().getResource(name);
        if (resource == null) throw new IllegalArgumentException("No such file: " + name);
        return new File(resource.toURI());
    }

    private byte[] contentOf(final File file) throws IOException, URISyntaxException {
        return Streams.toBytes(new FileInputStream(file));
    }

}