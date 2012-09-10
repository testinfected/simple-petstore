package test.integration.org.testinfected.petstore;

import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.petstore.FailureReporter;
import org.testinfected.petstore.PetStore;
import org.testinfected.petstore.Server;
import org.testinfected.petstore.WebLayout;
import org.testinfected.petstore.jdbc.DriverManagerDataSource;
import test.support.org.testinfected.petstore.jdbc.TestEnvironment;
import test.support.org.testinfected.petstore.web.Console;
import test.support.org.testinfected.petstore.web.HttpRequest;
import test.support.org.testinfected.petstore.web.HttpResponse;
import test.support.org.testinfected.petstore.web.LogFile;
import test.support.org.testinfected.petstore.web.WebRoot;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.isA;
import static test.support.org.testinfected.petstore.web.HttpRequest.aRequest;

@RunWith(JMock.class)
public class PetStoreTest {

    Mockery context = new JUnit4Mockery();
    FailureReporter failureReporter = context.mock(FailureReporter.class);
    DataSource dataSource = context.mock(DataSource.class);
    PetStore petstore = new PetStore(WebLayout.standard(WebRoot.locate()), dataSource);
    States database = context.states("database").startsAs("up");

    Connection connection;
    Console console = Console.captureStandardOutput();
    LogFile logFile;
    int serverPort = 9999;
    HttpRequest request = aRequest().onPort(serverPort);

    @Before public void
    startServer() throws Exception {
        connection = connectToTestDatabase();
        context.checking(new Expectations() {{
            allowing(dataSource).getConnection(); will(returnValue(connection)); when(database.is("up"));
            allowing(dataSource).getConnection(); will(throwException(new SQLException("Database is down"))); when(database.isNot("up"));
        }});

        logFile = LogFile.create();
        petstore.reportErrorsTo(failureReporter);
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

    // todo makes sense to make those end-to-end diagnostics tests?
    @Test public void
    canProduceAccessLogFile() throws IOException {
        petstore.logToFile(logFile.path());

        request.get("/products").assertOK();
        logFile.assertHasEntry(containsString("\"GET /products HTTP/1.1\" 200"));
    }

    @Test public void
    canOutputAccessLogToConsole() throws IOException {
        petstore.logToConsole();

        request.get("/products").assertOK();
        console.assertHasEntry(containsString("\"GET /products HTTP/1.1\" 200"));
    }

    @Test public void
    supportsHttpMethodOverride() throws IOException {
        petstore.logToConsole();

        request.withParameter("_method", "DELETE").post("/logout").assertOK();
        console.assertHasEntry(containsString("\"DELETE /logout HTTP/1.1\" 303"));
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
    renders404WhenAssetIsNotFound() throws IOException {
        HttpResponse response = request.get("/images/missing.png");

        response.assertHasStatusCode(404);
        response.assertHasNoHeader("Transfer-Encoding");
    }

    @Test public void
    renders404WhenNoRouteDefined() throws IOException {
        HttpResponse response = request.get("/unrecognized/route");

        response.assertHasStatusCode(404);
        response.assertHasNoHeader("Transfer-Encoding");
    }

    @Test public void
    renders500ErrorsAndReportsFailure() throws Exception {
        database.become("down");
        context.checking(new Expectations() {{
            oneOf(failureReporter).internalErrorOccurred(with(isA(SQLException.class)));
        }});

        HttpResponse response = request.get("/products");
        response.assertHasStatusCode(500);
        response.assertHasNoHeader("Transfer-Encoding");
        response.assertHasContent(containsString("Database is down"));
    }

    private Matcher<String> containsLayoutHeader() {
        return containsString("<div id=\"header\">");
    }

    private Connection connectToTestDatabase() throws SQLException {
        return DriverManagerDataSource.from(TestEnvironment.properties()).getConnection();
    }
}