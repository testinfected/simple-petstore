package test.integration.org.testinfected.molecule.simple;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Action;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.internal.ReturnDefaultValueAction;
import org.jmock.lib.action.ThrowAction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.molecule.Application;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;
import org.testinfected.molecule.simple.SimpleServer;
import org.testinfected.molecule.util.Charsets;
import org.testinfected.molecule.util.FailureReporter;
import org.testinfected.time.lib.BrokenClock;
import test.support.org.testinfected.molecule.integration.HttpRequest;
import test.support.org.testinfected.molecule.integration.HttpResponse;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.equalTo;
import static org.testinfected.time.lib.DateBuilder.aDate;
import static test.support.org.testinfected.molecule.integration.HttpRequest.aRequest;

@RunWith(JMock.class)
public class SimpleServerTest {
    Mockery context = new JUnit4Mockery();
    FailureReporter failureReporter = context.mock(FailureReporter.class);
    Application app = context.mock(Application.class);
    Exception error = new Exception("Error");

    SimpleServer server = new SimpleServer(9999);
    HttpRequest request = aRequest().to(server);
    HttpResponse response;

    String serverName = "server/version";
    Charset defaultCharset = Charsets.UTF_16;
    Date now = aDate().onCalendar(2012, 6, 8).atMidnight().build();

    @Before public void
    startServer() throws IOException {
        server.name(serverName);
        server.timeSource(BrokenClock.stoppedAt(now));
        server.reportErrorsTo(failureReporter);
        server.defaultCharset(defaultCharset);
        server.run(app);
    }

    @After public void
    stopServer() throws Exception {
        server.shutdown();
    }

    @Test public void
    setsDateHeader() throws Exception {
        application(succeeds());
        response = request.get("/");
        response.assertHasHeader("Date", "Fri, 08 Jun 2012 04:00:00 GMT");
    }

    @Test public void
    setsServerHeader() throws Exception {
        application(succeeds());
        response = request.get("/");
        response.assertHasHeader("Server", serverName);
    }

    @Test public void
    runsApplicationUsingDefaultCharsetForResponse() throws Exception {
        context.checking(new Expectations() {{
            oneOf(app).handle(with(anyRequest()), with(aResponseWithCharset(defaultCharset)));
        }});

        request.send();
    }

    @Test public void
    reportsFailureWhenAnErrorOccurs() throws Exception {
        application(failsWith(error));

        context.checking(new Expectations() {{
            oneOf(failureReporter).errorOccurred(with(error));
        }});

        request.send();
    }

    private Matcher<Request> anyRequest() {
        return any(Request.class);
    }

    private Matcher<Response> anyResponse() {
        return any(Response.class);
    }

    private Matcher<org.testinfected.molecule.Response> aResponseWithCharset(Charset charset) {
        return new FeatureMatcher<Response, Charset>(equalTo(charset), "response with charset", "charset") {
            protected Charset featureValueOf(org.testinfected.molecule.Response actual) {
                return actual.charset();
            }
        };
    }

    private void application(final Action action) throws Exception {
        context.checking(new Expectations() {{
            allowing(app).handle(with(anyRequest()), with(anyResponse())); will(action);
        }});
    }

    private Action succeeds() {
        return new ReturnDefaultValueAction();
    }

    private Action failsWith(final Exception failure) throws IOException {
        return new ThrowAction(failure);
    }
}
