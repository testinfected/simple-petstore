package test.unit.org.testinfected.support;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Action;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.internal.ReturnDefaultValueAction;
import org.jmock.lib.action.ThrowAction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.support.Application;
import org.testinfected.support.ApplicationContainer;
import org.testinfected.support.FailureReporter;
import org.testinfected.support.util.Charsets;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.hamcrest.Matchers.*;

@RunWith(JMock.class)
public class ApplicationContainerTest {

    Mockery context = new JUnit4Mockery();

    Application app = context.mock(Application.class);
    FailureReporter monitor = context.mock(FailureReporter.class);
    Charset defaultCharset = Charsets.UTF_16;
    ApplicationContainer applicationContainer = new ApplicationContainer(app, monitor, defaultCharset);

    Request request = context.mock(Request.class);
    Response response = context.mock(Response.class);

    @Test public void
    reportsInternalErrors() throws Exception {
        Exception error = new Exception("Application error");
        application(failsWith(error));
        communication(succeeds());
        expectInternalErrorReport(error);

        handleRequest();
    }

    @Test public void
    reportsCommunicationErrors() throws Exception {
        IOException error = new IOException("Communication error");
        application(succeeds());
        communication(failsWith(error));

        expectCommunicationFailureReport(error);

        handleRequest();
    }

    @Test public void
    runsApplicationAndUsesDefaultCharsetForResponse() throws Exception {
        contentTypeOfResponseIs(null);
        communication(succeeds());

        context.checking(new Expectations() {{
            one(app).handle(with(aRequestWrapping(request)), with(both(aResponseWrapping(response), aResponseWithCharset(defaultCharset))));
        }});

        handleRequest();
    }

    private void contentTypeOfResponseIs(final String type) {
        context.checking(new Expectations() {{
            allowing(response).getValue("Content-Type"); will(returnValue(type));
        }});
    }

    public Matcher<org.testinfected.support.Response> both(Matcher<org.testinfected.support.Response> first, Matcher<org.testinfected.support.Response> second) {
        return Matchers.allOf(first, second);
    }

    private Matcher<org.testinfected.support.Request> aRequest() {
        return any(org.testinfected.support.Request.class);
    }

    private Matcher<org.testinfected.support.Response> aResponse() {
        return any(org.testinfected.support.Response.class);
    }

    private Matcher<org.testinfected.support.Request> aRequestWrapping(Request request) {
        return new FeatureMatcher<org.testinfected.support.Request, Request>(sameInstance(request), "request wrapping", "wrapped request") {
            protected Request featureValueOf(org.testinfected.support.Request actual) {
                return actual.unwrap(Request.class);
            }
        };
    }

    private Matcher<org.testinfected.support.Response> aResponseWrapping(Response response) {
        return new FeatureMatcher<org.testinfected.support.Response, Response>(sameInstance(response), "response wrapping", "wrapped response") {
            protected Response featureValueOf(org.testinfected.support.Response actual) {
                return actual.unwrap(Response.class);
            }
        };
    }

    private Matcher<org.testinfected.support.Response> aResponseWithCharset(Charset charset) {
        return new FeatureMatcher<org.testinfected.support.Response, Charset>(equalTo(charset), "response with charset", "charset") {
            protected Charset featureValueOf(org.testinfected.support.Response actual) {
                return actual.charset();
            }
        };
    }

    private void application(final Action action) throws Exception {
        context.checking(new Expectations() {{
            allowing(app).handle(with(aRequest()), with(aResponse())); will(action);
        }});
    }

    private void communication(final Action action) throws Exception {
        context.checking(new Expectations() {{
            allowing(response).close(); will(action);
        }});
    }

    private Action succeeds() {
        return new ReturnDefaultValueAction();
    }

    private Action failsWith(final Exception failure) throws IOException {
        return new ThrowAction(failure);
    }

    private void expectInternalErrorReport(final Exception failure) {
        context.checking(new Expectations() {{
            oneOf(monitor).internalErrorOccurred(with(failure));
        }});
    }

    private void expectCommunicationFailureReport(final IOException failure) {
        context.checking(new Expectations() {{
            oneOf(monitor).communicationFailed(with(failure));
        }});
    }

    private void handleRequest() {
        applicationContainer.handle(request, response);
    }
}
