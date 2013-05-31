package test.unit.org.testinfected.molecule.middlewares;

import org.jmock.Expectations;
import org.jmock.States;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.testinfected.molecule.Application;
import org.testinfected.molecule.HttpException;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;
import org.testinfected.molecule.middlewares.ConnectionScope;
import test.support.org.testinfected.molecule.unit.MockRequest;
import test.support.org.testinfected.molecule.unit.MockResponse;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.fail;
import static test.support.org.testinfected.molecule.unit.MockRequest.aRequest;
import static test.support.org.testinfected.molecule.unit.MockResponse.aResponse;

public class ConnectionScopeTest {
    @Rule public JUnitRuleMockery context = new JUnitRuleMockery();

    DataSource dataSource = context.mock(DataSource.class);
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
            oneOf(connection).close(); when(connectionStatus.is("opened")); then(connectionStatus.is("closed"));
        }});
    }

    @Test public void
    opensConnectionAndMakesAvailableAsRequestAttribute() throws Exception {
        connectionScope.connectTo(reportPresenceOfAttribute(Connection.class, connection));
        connectionScope.handle(request, response);
        assertScoping("on");
    }

    @Test public void
    gracefullyClosesConnectionAndRemovesFromScopeWhenAnErrorOccurs() throws Exception {
        connectionScope.connectTo(crashWith(new HttpException("Boom!")));

        try {
            connectionScope.handle(request, response);
            fail("Exception did not bubble up");
        } catch (HttpException expected) {
        }

        request.assertAttribute(Connection.class, nullValue());
    }

    private Application crashWith(final HttpException error) {
        return new Application() {
            public void handle(Request request, Response response) throws Exception {
                throw error;
            }
        };
    }

    private Application reportPresenceOfAttribute(final Object key, final Object value) {
        return new Application() {
            public void handle(Request request, Response response) throws Exception {
                response.body(request.attribute(key) == value ? "on" : "off");
            }
        };
    }

    private void assertScoping(String state) {
        assertThat("scoping", response.body(), equalTo(state));
    }
}