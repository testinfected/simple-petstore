package test.integration.org.testinfected.petstore;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testinfected.petstore.PetStore;
import org.testinfected.petstore.Server;
import test.support.org.testinfected.petstore.web.Console;
import test.support.org.testinfected.petstore.web.HttpRequest;
import test.support.org.testinfected.petstore.web.HttpResponse;
import test.support.org.testinfected.petstore.web.LogFile;
import test.support.org.testinfected.petstore.web.WebRoot;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.containsString;
import static test.support.org.testinfected.petstore.web.HttpRequest.aRequest;

public class PetStoreTest {

    PetStore petstore = PetStore.at(WebRoot.locate());

    LogFile logFile;
    Console console = Console.captureStandardOutput();
    int serverPort = 9999;
    HttpRequest request = aRequest().onPort(serverPort);

    @Before public void
    startServer() throws IOException {
        logFile = LogFile.create();
        petstore.encodeOutputAs("utf-8");
        petstore.start(serverPort);
    }

    @After public void
    stopServer() throws Exception {
        petstore.stop();
        logFile.clear();
        console.terminate();
    }

    @Test public void
    setsServerHeaders() throws IOException {
        HttpResponse response = request.get("/");

        response.assertOK();
        response.assertHasHeader("Server", containsString(Server.NAME));
    }

    @Test public void
    canProduceAccessLogFile() throws IOException {
        petstore.logToFile(logFile.path());

        request.get("/home").assertOK();
        logFile.assertHasEntry(containsString("\"GET /home HTTP/1.1\" 200"));
    }

    @Test public void
    canOutputAccessLogToConsole() throws IOException {
        petstore.logToConsole();

        request.get("/cart").assertOK();
        console.assertHasEntry(containsString("\"GET /cart HTTP/1.1\" 200"));
    }

    @Test public void
    supportsHttpMethodOverride() throws IOException {
        petstore.logToConsole();

        request.withParameter("_method", "PUT").post("/item").assertOK();
        console.assertHasEntry(containsString("\"PUT /item HTTP/1.1\" 200"));
    }

    @Test public void
    rendersDynamicContentAsHtmlProperlyEncoded() throws IOException {
        HttpResponse response = request.get("/");

        response.assertOK();
        response.assertHasHeader("Content-Type", "text/html; charset=utf-8");
        response.assertContentIsEncodedAs("UTF-8");
        response.assertHasNoHeader("Transfer-Encoding");
    }

    @Test public void
    appliesLayoutToHtmlPages() throws IOException {
        HttpResponse response = request.get("/products");

        response.assertOK();
        response.assertHasContent(containsLayoutHeader());
    }

    @Test public void
    rendersStaticAssetsAsFiles() throws IOException {
        HttpResponse response = request.get("/images/logo.png");

        response.assertOK();
        response.assertHasHeader("Content-Type", "image/png");
        response.assertHasNoHeader("Transfer-Encoding");
    }

    @Test public void
    render404WhenResourceIsNotFound() throws IOException {
        HttpResponse response = request.get("/images/missing.png");

        response.assertNotFound();
        response.assertHasNoHeader("Transfer-Encoding");
    }

    private Matcher<String> containsLayoutHeader() {
        return containsString("<div id=\"header\">");
    }
}