package test.org.testinfected.petstore;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.petstore.Application;
import org.testinfected.petstore.ApplicationContainer;
import org.testinfected.petstore.FailureReporter;

import java.io.IOException;

public class ApplicationContainerTest {

    Mockery context = new JUnit4Mockery();

    Application app = context.mock(Application.class);
    FailureReporter monitor = context.mock(FailureReporter.class);
    ApplicationContainer applicationContainer = new ApplicationContainer(app, monitor);

    @Test public void
    reportInternalErrors() throws Exception {
        Exception error = new Exception("Application error");

        applicationFailsWith(error);
        communicationSucceeds();
        expectInternalErrorReport(error);

        handleRequest();

        context.assertIsSatisfied();
    }

    @Test public void
    reportCommunicationErrors() throws Exception {
        IOException error = new IOException("Communication error");

        applicationSucceeds();
        communicationFailsWith(error);
        expectCommunicationFailureReport(error);

        handleRequest();

        context.assertIsSatisfied();
    }

    private void applicationFailsWith(final Exception failure) throws Exception {
        context.checking(new Expectations() {{
            allowing(app).handle(with(same(request)), with(same(response)));
            will(throwException(failure));
        }});
    }

    private void expectInternalErrorReport(final Exception failure) {
        context.checking(new Expectations() {{
            oneOf(monitor).internalErrorOccurred(with(failure));
        }});
    }

    private void communicationSucceeds() throws IOException {
        context.checking(new Expectations() {{
            allowing(response).close();
        }});
    }

    private void applicationSucceeds() throws Exception {
        context.checking(new Expectations() {{
            allowing(app).handle(with(same(request)), with(same(response)));
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

    Request request = context.mock(Request.class);
    Response response = context.mock(Response.class);
}
