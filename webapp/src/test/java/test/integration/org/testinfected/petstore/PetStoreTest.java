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
import org.testinfected.petstore.PetStore;
import org.testinfected.petstore.Transactor;
import org.testinfected.petstore.UnitOfWork;
import org.testinfected.petstore.jdbc.JDBCTransactor;
import org.testinfected.petstore.jdbc.ProductsDatabase;
import org.testinfected.petstore.product.Product;
import org.testinfected.petstore.product.ProductCatalog;
import org.testinfected.molecule.simple.SimpleServer;
import org.testinfected.molecule.util.FailureReporter;
import test.support.org.testinfected.petstore.jdbc.Database;
import test.support.org.testinfected.petstore.jdbc.TestDatabaseEnvironment;
import test.support.org.testinfected.petstore.web.LogFile;
import test.support.org.testinfected.petstore.web.WebRoot;
import test.support.org.testinfected.molecule.integration.HttpRequest;
import test.support.org.testinfected.molecule.integration.HttpResponse;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.FileHandler;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.isA;
import static test.support.org.testinfected.petstore.builders.ProductBuilder.aProduct;
import static test.support.org.testinfected.molecule.integration.HttpRequest.aRequest;

@RunWith(JMock.class)
public class PetStoreTest {

    Mockery context = new JUnit4Mockery();
    FailureReporter failureReporter = context.mock(FailureReporter.class);
    DataSource dataSource = context.mock(DataSource.class);
    PetStore petstore = new PetStore(WebRoot.locate(), dataSource);

    States databaseStatus = context.states("database").startsAs("up");
    Database database = Database.in(TestDatabaseEnvironment.load());
    Connection connection = database.connect();
    Transactor transactor = new JDBCTransactor(connection);
    ProductCatalog productCatalog = new ProductsDatabase(connection);

    LogFile logFile;
    int serverPort = 9999;
    SimpleServer server = new SimpleServer(serverPort);
    HttpRequest request = aRequest().onPort(serverPort);

    String encoding = "utf-16";

    @Before public void
    startServer() throws Exception {
        context.checking(new Expectations() {{
            allowing(dataSource).getConnection(); will(returnValue(connection)); when(databaseStatus.is("up"));
            allowing(dataSource).getConnection(); will(throwException(new SQLException("Database is down"))); when(databaseStatus.isNot("up"));
        }});
        database.clean();

        server.defaultCharset(Charset.forName(encoding));
        logFile = LogFile.create();
        petstore.logTo(new FileHandler(logFile.path()));
        petstore.reportErrorsTo(failureReporter);
        petstore.start(server);
    }

    @After public void
    stopServer() throws Exception {
        server.shutdown();
        logFile.clear();
    }

    @Test public void
    logsAllAccesses() throws IOException {
        request.get("/products").assertOK();
        logFile.assertHasEntry(containsString("\"GET /products HTTP/1.1\" 200"));
    }

    @Test public void
    supportsHttpMethodOverride() throws IOException {
        request.withParameter("_method", "DELETE").post("/logout").assertOK();
    }

    @Test public void
    rendersDynamicContentAsHtmlProperlyEncoded() throws Exception {
        addProducts(aProduct().named("French Bouledogue (Bouledogue français)").build());
        HttpResponse response = request.get("/products?keyword=bouledogue");

        response.assertOK();
        response.assertHasContent(productsList());
        response.assertHasContentType("text/html; charset=" + encoding);
        response.assertContentIsEncodedAs(encoding);
        response.assertChunked();
    }

    @Test public void
    appliesLayoutToHtmlPages() throws IOException {
        HttpResponse response = request.get("/");

        response.assertOK();
        response.assertHasContent(layoutHeader());
        response.assertChunked();
    }

    @Test public void
    rendersStaticAssetsAsFiles() throws IOException {
        HttpResponse response = request.get("/images/logo.png");

        response.assertOK();
        response.assertHasContentType("image/png");
        response.assertNotChunked();
    }

    @Test public void
    renders404WhenAssetIsNotFound() throws IOException {
        HttpResponse response = request.get("/images/missing.png");

        response.assertHasStatusCode(404);
    }

    @Test public void
    renders404WhenNoRouteDefined() throws IOException {
        HttpResponse response = request.get("/unrecognized/route");

        response.assertHasStatusCode(404);
    }

    @Test public void
    renders500ErrorsAndReportsFailureWhenSomethingGoesWrong() throws Exception {
        databaseStatus.become("down");
        context.checking(new Expectations() {{
            oneOf(failureReporter).errorOccurred(with(isA(SQLException.class)));
        }});

        HttpResponse response = request.get("/products");
        response.assertHasStatusCode(500);
        response.assertHasContent(containsString("Database is down"));
    }

    private Matcher<String> productsList() {
        return containsString("<div id=\"products\">");
    }

    private Matcher<String> layoutHeader() {
        return containsString("<div id=\"header\">");
    }

    private void addProducts(final Product... products) throws Exception {
        transactor.perform(new UnitOfWork() {
            public void execute() throws Exception {
                for (Product product : products) {
                    productCatalog.add(product);
                }
            }
        });
    }
}