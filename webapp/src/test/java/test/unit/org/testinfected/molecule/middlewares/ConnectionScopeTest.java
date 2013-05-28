package test.unit.org.testinfected.molecule.middlewares;

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
import test.support.org.testinfected.molecule.unit.MockRequest;
import test.support.org.testinfected.molecule.unit.MockResponse;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.fail;
import static test.support.org.testinfected.molecule.unit.MockRequest.aRequest;
import static test.support.org.testinfected.molecule.unit.MockResponse.aResponse;

@RunWith(JMock.class)
public class ConnectionScopeTest {

    Mockery context = new JUnit4Mockery();
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
            oneOf(connection).close(); when(connectionStatus.is("opened"));
                then(connectionStatus.is("closed"));
        }});
    }

    @Test public void
    opensConnectionAndMakesAvailableAsRequestAttribute() throws Exception {
        connectionScope.connectTo(new Application() {
            public void handle(Request request, Response response) throws Exception {
                response.body("Connection scoping " + (request.attribute(Connection.class) == connection ? "on" : "off"));
            }
        });

        connectionScope.handle(request, response);
        response.assertBody("Connection scoping on");
    }

    @Test public void
    gracefullyClosesConnectionAndRemovesFromScopeWhenAnErrorOccurs() throws Exception {
        connectionScope.connectTo(new Application() {
            public void handle(Request request, Response response) throws Exception {
                throw new HttpException("Boom!");
            }
        });

        try {
            connectionScope.handle(request, response);
            fail("Exception did not bubble up");
        } catch (HttpException expected) {
        }

        request.assertAttribute(Connection.class, nullValue());
    }
}