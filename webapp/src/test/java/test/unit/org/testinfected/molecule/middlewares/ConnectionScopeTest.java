package test.unit.org.testinfected.molecule.middlewares;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.molecule.Application;
import org.testinfected.molecule.HttpException;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;
import org.testinfected.molecule.middlewares.ConnectionScope;
import test.support.org.testinfected.molecule.web.MockRequest;
import test.support.org.testinfected.molecule.web.MockResponse;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.jmock.Expectations.same;
import static test.support.org.testinfected.molecule.web.MockRequest.aRequest;
import static test.support.org.testinfected.molecule.web.MockResponse.aResponse;

@RunWith(JMock.class)
public class ConnectionScopeTest {

    Mockery context = new JUnit4Mockery();
    DataSource dataSource = context.mock(DataSource.class);
    Application successor = context.mock(Application.class);
    ConnectionScope connectionScope = new ConnectionScope(dataSource);

    Connection connection = context.mock(Connection.class);
    States connectionStatus = context.states("connection").startsAs("closed");

    MockRequest request = aRequest();
    MockResponse response = aResponse();

    @Before public void
    chainWithSuccessor() throws Exception {
        context.checking(new Expectations() {{
            allowing(dataSource).getConnection(); will(returnValue(connection)); when(connectionStatus.is("closed"));
                then(connectionStatus.is("opened"));
            oneOf(connection).close(); when(connectionStatus.is("opened"));
                then(connectionStatus.is("closed"));
        }});

        connectionScope.connectTo(successor);
    }

    @Test public void
    makesConnectionAvailableToSuccessor() throws Exception {
        context.checking(new Expectations() {{
            oneOf(successor).handle(with(aRequestWithAttribute(Connection.class, sameConnection(connection))), with(any(Response.class))); when(connectionStatus.is("opened"));
        }});

        connectionScope.handle(request, response);
    }

    @Test public void
    gracefullyClosesConnectionAndRemovesFromScopeWhenAnErrorOccurs() throws Exception {
        context.checking(new Expectations() {{
            allowing(successor).handle(with(any(Request.class)), with(any(Response.class)));
                will(throwException(new HttpException("error")));
        }});

        try {
            connectionScope.handle(request, response);
        } catch (HttpException expected) {
        }

        request.assertAttribute(Connection.class, Matchers.nullValue());
    }

    private Matcher<Object> sameConnection(final Connection connection) {
        return same((Object) connection);
    }

    private Matcher<Request> aRequestWithAttribute(final Object attribute, Matcher<Object> connection) {
        return new FeatureMatcher<Request, Object>(connection, "a request with attribute " + attribute, attribute.toString()) {
            protected Object featureValueOf(Request actual) {
                return actual.attribute(attribute);
            }
        };
    }
}