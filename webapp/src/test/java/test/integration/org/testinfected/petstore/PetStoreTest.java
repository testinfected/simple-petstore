package test.integration.org.testinfected.petstore;

import com.vtence.molecule.FailureReporter;
import com.vtence.molecule.WebServer;
import com.vtence.molecule.testing.http.Form;
import com.vtence.molecule.testing.http.HttpRequest;
import com.vtence.molecule.testing.http.HttpResponse;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.States;
import org.jmock.api.Action;
import org.jmock.api.Invocation;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.action.CustomAction;
import org.jmock.lib.concurrent.Synchroniser;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.testinfected.petstore.PetStore;
import org.testinfected.petstore.db.ItemsDatabase;
import org.testinfected.petstore.db.JDBCTransactor;
import org.testinfected.petstore.db.ProductsDatabase;
import org.testinfected.petstore.product.Product;
import org.testinfected.petstore.util.Logging;
import test.support.org.testinfected.petstore.StackTrace;
import test.support.org.testinfected.petstore.builders.ItemBuilder;
import test.support.org.testinfected.petstore.builders.ProductBuilder;
import test.support.org.testinfected.petstore.jdbc.Database;
import test.support.org.testinfected.petstore.jdbc.TestDatabaseEnvironment;
import test.support.org.testinfected.petstore.web.LogFile;
import test.support.org.testinfected.petstore.web.WebRoot;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static com.vtence.molecule.testing.http.HttpResponseAssert.assertThat;
import static java.util.concurrent.TimeUnit.MINUTES;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.fail;
import static test.support.org.testinfected.petstore.builders.ItemBuilder.anItem;
import static test.support.org.testinfected.petstore.builders.ProductBuilder.aProduct;

public class PetStoreTest {

    static final String SESSION_COOKIE = "molecule.session";
    static final int SESSION_TIMEOUT = (int) MINUTES.toSeconds(30);

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery() {{
        setThreadingPolicy(new Synchroniser());
    }};

    FailureReporter failureReporter = context.mock(FailureReporter.class);
    DataSource dataSource = context.mock(DataSource.class);
    PetStore petstore = new PetStore(WebRoot.locate(), dataSource);

    States system = context.states("system").startsAs("up");
    Database database = Database.in(TestDatabaseEnvironment.load());
    Connection connection = database.connect();

    LogFile logFile;
    WebServer server = WebServer.create("localhost", 9999);
    HttpRequest request = new HttpRequest(server.uri().getPort());
    HttpResponse response;

    Exception error;

    @Before
    public void
    startServer() throws Exception {
        context.checking(new Expectations() {{
            allowing(dataSource).getConnection(); will(openConnection());
                when(system.is("up"));
            allowing(dataSource).getConnection(); will(throwException(new SQLException("Database is down")));
                when(system.isNot("up"));
        }});
        database.clean();

        logFile = LogFile.create();
        petstore.logging(Logging.toFile(logFile.path()));
        petstore.sessionTimeout(SESSION_TIMEOUT);

        context.checking(new Expectations() {{
            allowing(failureReporter).errorOccurred(with(any(Exception.class))); will(captureInternalError());
                when(system.is("up"));
        }});

        petstore.reportErrorsTo(failureReporter);
        petstore.start(server);
    }

    @After
    public void
    stopServer() throws Exception {
        connection.close();
        server.stop();
        logFile.clear();
    }

    @Test
    public void
    setsServerHeader() throws Exception {
        response = request.get("/");
        assertOK();
        assertThat(response).hasHeader("Server", PetStore.NAME);
    }

    @Test
    public void
    setsDateHeader() throws Exception {
        response = request.get("/");
        assertOK();
        assertThat(response).hasHeader("Date", notNullValue());
    }

    @Test
    public void
    logsAllAccesses() throws Exception {
        response = request.get("/products");
        assertOK();
        logFile.assertHasEntry(containsString("\"GET /products HTTP/1.1\" 200"));
    }

    @Test
    public void
    supportsHttpMethodOverride() throws IOException {
        response = request.content(Form.urlEncoded().addField("_method", "DELETE")).post("/logout");
        assertRedirected();
    }

    @Test
    public void
    rendersDynamicContentAsHtmlUtf8Encoded() throws Exception {
        makeProducts(aProduct().named("French Bouledogue (Bouledogue français)"));
        response = request.get("/products?keyword=bouledogue");

        assertOK();
        assertThat(response).hasBodyText(productsList())
                            .hasContentType("text/html; charset=utf-8")
                            .hasContentEncodedAs("utf-8")
                            .isChunked();
    }

    @Test
    public void
    appliesLayoutToHtmlPages() throws IOException {
        response = request.get("/");

        assertOK();
        assertThat(response).hasBodyText(layoutHeader())
                .isChunked();
    }

    @Test
    public void
    rendersStaticAssetsAsFiles() throws IOException {
        response = request.get("/images/logo.png");

        assertOK();
        assertThat(response).hasContentType("image/png")
                            .hasBodySize(26427)
                            .isNotChunked();
    }

    @Test
    public void
    renders404WhenAssetIsNotFound() throws IOException {
        response = request.get("/images/missing.png");
        assertNotFound();
        assertThat(response).isNotChunked();
    }

    @Test
    public void
    renders404WhenNoRouteDefined() throws IOException {
        response = request.get("/unrecognized/route");
        assertNotFound();
        assertThat(response).isNotChunked();
    }

    @Test
    public void
    renders500AndReportsFailureWhenSomethingGoesWrong() throws Exception {
        system.become("down");
        context.checking(new Expectations() {{
            oneOf(failureReporter).errorOccurred(with(isA(SQLException.class)));
                when(system.isNot("up"));
        }});

        response = request.get("/products");
        assertThat(response).hasStatusCode(500)
                            .hasBodyText(containsString("Database is down"))
                            .isNotChunked();
    }

    @Test
    public void
    maintainsSessionsAcrossRequestsUsingCookies() throws Exception {
        makeItems(anItem().of(make(aProduct().named("Gecko"))).withNumber("12345678"));

        response = request.but().content(Form.urlEncoded().addField("item-number", "12345678"))
                          .post("/cart");
        assertRedirected();
        assertThat(response).hasCookie(SESSION_COOKIE).hasMaxAge(SESSION_TIMEOUT);

        String sessionCookie = response.cookie(SESSION_COOKIE).getValue();
        response = request.but().cookie(SESSION_COOKIE, sessionCookie).get("/cart");
        assertOK();
        assertThat(response).hasBodyText(containsString("cart-item-12345678"))
                            .hasCookie(SESSION_COOKIE).hasMaxAge(SESSION_TIMEOUT);
    }

    private void assertOK() {
        assertNoError();
        assertThat(response).isOK();
    }

    private void assertNotFound() {
        assertNoError();
        assertThat(response).hasStatusCode(404);
    }

    private void assertRedirected() {
        assertNoError();
        assertThat(response).hasStatusCode(303);
    }

    private void assertNoError() {
        if (error != null) fail(StackTrace.of(error));
    }

    private Matcher<String> productsList() {
        return containsString("<div id=\"products\">");
    }

    private Matcher<String> layoutHeader() {
        return containsString("<div id=\"header\">");
    }

    private Product make(final ProductBuilder builder) throws Exception {
        final Product product = builder.build();
        new JDBCTransactor(connection).perform(() -> new ProductsDatabase(connection).add(product));
        return product;
    }

    private void makeProducts(final ProductBuilder... products) throws Exception {
        new JDBCTransactor(connection).perform(() -> {
            for (ProductBuilder each : products) {
                new ProductsDatabase(connection).add(each.build());
            }
        });
    }

    private void makeItems(final ItemBuilder... items) throws Exception {
        new JDBCTransactor(connection).perform(() -> {
            for (ItemBuilder each : items) {
                new ItemsDatabase(connection).add(each.build());
            }
        });
    }

    private Action openConnection() {
        return new CustomAction("open connection") {
            public Object invoke(Invocation invocation) throws Throwable {
                return database.connect();
            }
        };
    }

    private CustomAction captureInternalError() {
        return new CustomAction("capture internal error") {
            public Object invoke(Invocation invocation) throws Throwable {
                error = (Exception) invocation.getParameter(0);
                return null;
            }
        };
    }
}
