package test.unit.org.testinfected.molecule.middlewares;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
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
import org.testinfected.molecule.middlewares.ConnectionScope;
import test.support.org.testinfected.molecule.unit.MockRequest;
import test.support.org.testinfected.molecule.unit.MockResponse;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.fail;
import static test.support.org.testinfected.molecule.unit.MockRequest.aRequest;
import static test.support.org.testinfected.molecule.unit.MockResponse.aResponse;

@RunWith(JMock.class)
public class ConnectionScopeTest {

    Mockery context = new JUnit4Mockery();
    DataSource dataSource = context.mock(DataSource.class);
    Application successor = context.mock(Application.class, "successor");
    ConnectionScope connectionScope = new ConnectionScope(dataSource);

    Connection connection = context.mock(Connection.class);
    States connectionStatus = context.states("connection").startsAs("closed");

    MockRequest request = aRequest();
    MockResponse response = aResponse();

    @Before public void
    expectConnectionToBeReleased() throws Exception {
        context.checking(new Expectations() {{
            allowing(dataSource).getConnection(); will(returnValue(connection)); when(connectionStatus.is("closed"));
                then(connectionStatus.is("opened"));
            oneOf(connection).close(); when(connectionStatus.is("opened"));
                then(connectionStatus.is("closed"));
        }});
    }

    @Before public void
    connectToSuccessor() {
        connectionScope.connectTo(successor);
    }

    @Test public void
    opensConnectionAndMakesAvailableAsRequestAttribute() throws Exception {
        context.checking(new Expectations() {{
            oneOf(successor).handle(with(requestWithAttribute(Connection.class, sameAs(connection))), with(response));
        }});

        connectionScope.handle(request, response);
    }

    @Test public void
    gracefullyClosesConnectionAndRemovesFromScopeWhenAnErrorOccurs() throws Exception {
        context.checking(new Expectations() {{
            allowing(successor).handle(request, response); will(throwException(new HttpException("error")));
        }});

        try {
            connectionScope.handle(request, response);
            fail("Exception did not bubble up");
        } catch (HttpException expected) {
        }

        request.assertAttribute(Connection.class, nullValue());
    }

    private Matcher<Object> sameAs(final Object connection) {
        return sameInstance(connection);
    }

    private Matcher<Request> requestWithAttribute(final Object key, Matcher<Object> valueMatcher) {
        return new FeatureMatcher<Request, Object>(valueMatcher, "a request with attribute " + key, key.toString()) {
            protected Object featureValueOf(Request actual) {
                return actual.attribute(key);
            }
        };
    }
}