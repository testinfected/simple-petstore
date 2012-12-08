package test.unit.org.testinfected.molecule.middlewares;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.molecule.Application;
import org.testinfected.molecule.middlewares.FailureMonitor;
import org.testinfected.molecule.util.FailureReporter;
import test.support.org.testinfected.molecule.web.MockRequest;
import test.support.org.testinfected.molecule.web.MockResponse;

import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static test.support.org.testinfected.molecule.web.MockRequest.aRequest;
import static test.support.org.testinfected.molecule.web.MockResponse.aResponse;

@RunWith(JMock.class)
public class FailureMonitorTest {

    Mockery context = new JUnit4Mockery();
    FailureReporter failureReporter = context.mock(FailureReporter.class);
    Application successor = context.mock(Application.class);
    FailureMonitor monitor = new FailureMonitor(failureReporter);

    Exception error = new Exception("An internal error occurred!");

    MockRequest request = aRequest();
    MockResponse response = aResponse();

    @Before public void
    chainMiddlewares() {
        monitor.connectTo(successor);
    }

    @Test public void
    notifiesFailureReporterAndRethrowsExceptionInCaseOfError() throws Exception {
        context.checking(new Expectations() {{
            allowing(successor).handle(with(request), with(response)); will(throwException(error));
            oneOf(failureReporter).errorOccurred(with(same(error)));
        }});

        try {
            monitor.handle(request, response);
            fail("Exception did not bubble up");
        } catch (Exception e) {
            assertThat("error", e, sameInstance(error));
        }
    }
}
