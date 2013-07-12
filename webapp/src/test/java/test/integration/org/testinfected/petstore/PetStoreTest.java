package test.integration.org.testinfected.petstore;

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
import org.testinfected.molecule.session.CookieTracker;
import org.testinfected.molecule.session.SessionPool;
import org.testinfected.molecule.simple.SimpleServer;
import org.testinfected.molecule.util.Clock;
import org.testinfected.molecule.util.FailureReporter;
import org.testinfected.petstore.PetStore;
import org.testinfected.petstore.db.ItemsDatabase;
import org.testinfected.petstore.db.JDBCTransactor;
import org.testinfected.petstore.db.ProductsDatabase;
import org.testinfected.petstore.product.Product;
import org.testinfected.petstore.transaction.UnitOfWork;
import test.support.org.testinfected.molecule.integration.HttpRequest;
import test.support.org.testinfected.molecule.integration.HttpResponse;
import test.support.org.testinfected.molecule.unit.BrokenClock;
import test.support.org.testinfected.petstore.StackTrace;
import test.support.org.testinfected.petstore.builders.ItemBuilder;
import test.support.org.testinfected.petstore.builders.ProductBuilder;
import test.support.org.testinfected.petstore.jdbc.Database;
import test.support.org.testinfected.petstore.jdbc.TestDatabaseEnvironment;
import test.support.org.testinfected.petstore.web.LogFile;
import test.support.org.testinfected.petstore.web.WebRoot;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.FileHandler;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.fail;
import static test.support.org.testinfected.molecule.integration.HttpRequest.aRequest;
import static test.support.org.testinfected.molecule.unit.DateBuilder.calendarDate;
import static test.support.org.testinfected.petstore.builders.ItemBuilder.anItem;
import static test.support.org.testinfected.petstore.builders.ProductBuilder.aProduct;

public class PetStoreTest {

    static final String SESSION_COOKIE = "JSESSIONID";
    static final long SESSION_TIMEOUT = MINUTES.toSeconds(30);

    @Rule public JUnitRuleMockery context = new JUnitRuleMockery() {{
        setThreadingPolicy(new Synchroniser());
    }};

    FailureReporter failureReporter = context.mock(FailureReporter.class);
    DataSource dataSource = context.mock(DataSource.class);
    Delorean delorean = new Delorean();
    PetStore petstore = new PetStore(WebRoot.locate(), dataSource);

    States system = context.states("system").startsAs("up");
    Database database = Database.in(TestDatabaseEnvironment.load());
    Connection connection = database.connect();

    LogFile logFile;
    int serverPort = 9999;
    SimpleServer server = new SimpleServer(serverPort);
    HttpRequest request = aRequest().onPort(serverPort);
    HttpResponse response;

    Exception error;

    String encoding = "utf-16";
    Date now = calendarDate(2012, 6, 8).atMidnight().inZone("GMT-04:00").build();

    @Before public void
    startServer() throws Exception {
        context.checking(new Expectations() {{
            allowing(dataSource).getConnection(); will(openConnection()); when(system.is("up"));
            allowing(dataSource).getConnection(); will(throwException(new SQLException("Database is down"))); when(system.isNot("up"));
        }});
        database.clean();

        server.enableSessions(new CookieTracker(new SessionPool(delorean, SESSION_TIMEOUT)));
        server.defaultCharset(Charset.forName(encoding));
        petstore.setClock(BrokenClock.stoppedAt(now));
        logFile = LogFile.create();
        petstore.logTo(new FileHandler(logFile.path()));

        context.checking(new Expectations() {{
            allowing(failureReporter).errorOccurred(with(any(Exception.class))); will(captureInternalError()); when(system.is("up"));
        }});

        petstore.reportErrorsTo(failureReporter);
        petstore.start(server);
    }

    @After public void
    stopServer() throws Exception {
        delorean.back();
        connection.close();
        server.shutdown();
        logFile.clear();
    }

    @Test public void
    setsServerHeader() throws Exception {
        response = request.get("/");
        assertOK();
        response.assertHasHeader("Server", PetStore.NAME);
    }

    @Test public void
    setsDateHeader() throws Exception {
        response = request.get("/");
        assertOK();
        response.assertHasHeader("Date", "Fri, 08 Jun 2012 04:00:00 GMT");
    }

    @Test public void
    logsAllAccesses() throws Exception {
        response = request.get("/products");
        assertOK();
        logFile.assertHasEntry(containsString("\"GET /products HTTP/1.1\" 200"));
    }

    @Test public void
    supportsHttpMethodOverride() throws IOException {
        response = request.withParameter("_method", "DELETE").post("/logout");
        assertOK();
    }

    @Test public void
    rendersDynamicContentAsHtmlProperlyEncoded() throws Exception {
        makeProducts(aProduct().named("French Bouledogue (Bouledogue français)"));
        response = request.get("/products?keyword=bouledogue");

        assertOK();
        response.assertHasContent(productsList());
        response.assertHasContentType("text/html; charset=" + encoding);
        response.assertContentIsEncodedAs(encoding);
        response.assertChunked();
    }

    @Test public void
    appliesLayoutToHtmlPages() throws IOException {
        response = request.get("/");

        assertOK();
        response.assertHasContent(layoutHeader());
        response.assertChunked();
    }

    @Test public void
    rendersStaticAssetsAsFiles() throws IOException {
        response = request.get("/images/logo.png");

        assertOK();
        response.assertHasContentType("image/png");
        response.assertNotChunked();
    }

    @Test public void
    renders404WhenAssetIsNotFound() throws IOException {
        response = request.get("/images/missing.png");
        assertNotFound();
    }

    @Test public void
    renders404WhenNoRouteDefined() throws IOException {
        response = request.get("/unrecognized/route");
        assertNotFound();
    }

    @Test public void
    renders500AndReportsFailureWhenSomethingGoesWrong() throws Exception {
        system.become("down");
        context.checking(new Expectations() {{
            oneOf(failureReporter).errorOccurred(with(isA(SQLException.class))); when(system.isNot("up"));
        }});

        response = request.get("/products");
        response.assertHasStatusCode(500);
        response.assertHasContent(containsString("Database is down"));
    }

    @Test public void
    createsSessionsOnDemandAndMaintainSessionsAcrossRequestsUsingCookies() throws Exception {
        makeItems(anItem().of(make(aProduct().named("Gecko"))).withNumber("12345678"));

        response = request.get("/");
        assertOK();
        response.assertHasNoCookie(SESSION_COOKIE);

        response = request.withParameter("item-number", "12345678").followRedirects(false).post("/cart");
        assertRedirected();
        response.assertHasCookie(SESSION_COOKIE);

        response = request.get("/cart");
        assertOK();
        response.assertHasContent(containsString("cart-item-12345678"));
        response.assertHasNoCookie(SESSION_COOKIE);
    }

    @Test public void
    sessionsExpireAfterTimeout() throws Exception {
        makeItems(anItem().of(make(aProduct().named("Gecko"))).withNumber("12345678"));

        response = request.withParameter("item-number", "12345678").post("/cart");
        assertOK();

        delorean.travel(SECONDS.toMillis(SESSION_TIMEOUT));
        response = request.get("/cart");
        assertOK();
        response.assertHasContent(not(containsString("cart-item-12345678")));
        response.assertHasCookie(SESSION_COOKIE);
    }

    private void assertOK() {
        assertNoError();
        response.assertOK();
    }

    private void assertNotFound() {
        assertNoError();
        response.assertHasStatusCode(404);
    }

    private void assertRedirected() {
        assertNoError();
        response.assertHasStatusCode(303);
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
        new JDBCTransactor(connection).perform(new UnitOfWork() {
            public void execute() throws Exception {
                new ProductsDatabase(connection).add(product);
            }
        });
        return product;
    }

    private void makeProducts(final ProductBuilder... products) throws Exception {
        new JDBCTransactor(connection).perform(new UnitOfWork() {
            public void execute() throws Exception {
                for (ProductBuilder each : products) {
                    new ProductsDatabase(connection).add(each.build());
                }
            }
        });
    }

    private void makeItems(final ItemBuilder... items) throws Exception {
        new JDBCTransactor(connection).perform(new UnitOfWork() {
            public void execute() throws Exception {
                for (ItemBuilder each : items) {
                    new ItemsDatabase(connection).add(each.build());
                }
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

    private static class Delorean implements Clock {

        private long timeTravel = 0;

        public Date now() {
            return new Date(System.currentTimeMillis() + timeTravel);
        }

        public void travel(long offsetInMillis) {
            this.timeTravel = offsetInMillis;
        }

        public void back() {
            travel(0);
        }
    }
}
