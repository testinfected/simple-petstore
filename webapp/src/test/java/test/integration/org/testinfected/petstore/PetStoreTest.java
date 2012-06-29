package test.integration.org.testinfected.petstore;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testinfected.petstore.PetStore;
import org.testinfected.petstore.Server;
import test.support.org.testinfected.petstore.web.Console;
import test.support.org.testinfected.petstore.web.HttpResponse;
import test.support.org.testinfected.petstore.web.LogFile;
import test.support.org.testinfected.petstore.web.OfflineContext;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.containsString;
import static test.support.org.testinfected.petstore.web.HttpRequest.get;
import static test.support.org.testinfected.petstore.web.OfflineContext.fromSystemProperties;

public class PetStoreTest {

    PetStore petstore = new PetStore(fromSystemProperties().webRoot());

    LogFile logFile;
    Console console = Console.captureStandardOutput();

    @Before public void
    startServer() throws IOException {
        logFile = LogFile.create();
        petstore.encodeOutputAs("utf-8");
        petstore.start(OfflineContext.TEST_PORT);
    }

    @After public void
    stopServer() throws Exception {
        petstore.stop();
        logFile.clear();
        console.terminate();
    }

    @Test public void
    setsServerHeaders() throws IOException {
        HttpResponse response = get("/");

        response.assertOK();
        response.assertHasHeader("Server", containsString(Server.NAME));
    }

    @Test public void
    canProduceAccessLogFile() throws IOException {
        petstore.logToFile(logFile.path());

        get("/home").assertOK();
        logFile.assertHasEntry(containsString("\"GET /home HTTP/1.1\" 200"));
    }

    @Test public void
    canOutputAccessLogToConsole() throws IOException {
        petstore.logToConsole();

        get("/cart").assertOK();
        console.assertHasEntry(containsString("\"GET /cart HTTP/1.1\" 200"));
    }

    @Test public void
    rendersDynamicContentAsHtmlProperlyEncoded() throws IOException {
        HttpResponse response = get("/");

        response.assertOK();
        response.assertHasHeader("Content-Type", "text/html; charset=utf-8");
        response.assertContentIsEncodedAs("UTF-8");
        response.assertHasNoHeader("Transfer-Encoding");
    }

    @Test public void
    rendersStaticAssetsAsFiles() throws IOException {
        HttpResponse response = get("/images/logo.png");

        response.assertOK();
        response.assertHasHeader("Content-Type", "image/png");
        response.assertHasNoHeader("Transfer-Encoding");
    }

    @Test public void
    render404WhenResourceIsNotFound() throws IOException {
        HttpResponse response = get("/images/missing.png");

        response.assertNotFound();
        response.assertHasNoHeader("Transfer-Encoding");
    }

}