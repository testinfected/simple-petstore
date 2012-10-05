package test.unit.org.testinfected.petstore;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.petstore.Application;
import org.testinfected.petstore.ApplicationContainer;
import org.testinfected.petstore.FailureReporter;

import java.io.IOException;

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
        IOException error = new IOException("Communication error");

        applicationWillSucceed();
        communicationWillFailWith(error);
        expectCommunicationFailureReport(error);

        handleRequest();
    }

    private void applicationWillFailWith(final Exception failure) throws Exception {
        context.checking(new Expectations() {{
            allowing(app).handle(with(same(request)), with(same(response))); will(throwException(failure));
        }});
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

    private void applicationWillSucceed() throws Exception {
        context.checking(new Expectations() {{
            allowing(app).handle(with(same(request)), with(same(response)));
        }});
    }

    private void communicationWillFailWith(final IOException failure) throws IOException {
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
