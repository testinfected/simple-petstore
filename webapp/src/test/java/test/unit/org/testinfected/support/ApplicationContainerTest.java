package test.unit.org.testinfected.support;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.support.Application;
import org.testinfected.support.ApplicationContainer;
import org.testinfected.support.FailureReporter;

import java.io.IOException;

import static org.hamcrest.Matchers.sameInstance;

@RunWith(JMock.class)
public class ApplicationContainerTest {

    Mockery context = new JUnit4Mockery();

    Application app = context.mock(Application.class);
    FailureReporter monitor = context.mock(FailureReporter.class);
    ApplicationContainer applicationContainer = new ApplicationContainer(app, monitor);

    Request request = context.mock(Request.class);
    Response response = context.mock(Response.class);

    @Test public void
    reportsInternalErrors() throws Exception {
        Exception error = new Exception("Application error");

        applicationWillFailWith(error);
        communicationWillSucceed();
        expectInternalErrorReport(error);

        handleRequest();
    }

    @Test public void
    reportsCommunicationErrors() throws Exception {
        applicationSucceeds();
        IOException error = new IOException("Communication error");
        communicationFailsWith(error);

        expectCommunicationFailureReport(error);

        handleRequest();
    }

    private void applicationWillFailWith(final Exception failure) throws Exception {
        context.checking(new Expectations() {{
            allowing(app).handle(with(aRequestWrapping(request)), with(aResponseWrapping(response))); will(throwException(failure));
        }});
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

    private void expectInternalErrorReport(final Exception failure) {
        context.checking(new Expectations() {{
            oneOf(monitor).internalErrorOccurred(with(failure));
        }});
    }

    private void communicationWillSucceed() throws IOException {
        context.checking(new Expectations() {{
            allowing(response).close();
        }});
    }

    private void applicationSucceeds() throws Exception {
        context.checking(new Expectations() {{
            allowing(app).handle(with(aRequestWrapping(request)), with(aResponseWrapping(response)));
        }});
    }

    private void communicationFailsWith(final IOException failure) throws IOException {
        context.checking(new Expectations() {{
            allowing(response).close(); will(throwException(failure));
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
