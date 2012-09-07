package test.integration.org.testinfected.petstore.pipeline;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.petstore.Application;
import org.testinfected.petstore.Server;
import org.testinfected.petstore.pipeline.ConnectionManager;
import org.testinfected.petstore.pipeline.MiddlewareStack;
import test.support.org.testinfected.petstore.web.HttpRequest;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.jmock.Expectations.same;
import static test.support.org.testinfected.petstore.web.HttpRequest.aRequest;

@RunWith(JMock.class)
public class ConnectionManagerTest {

    Mockery context = new JUnit4Mockery();
    DataSource dataSource = context.mock(DataSource.class);
    Connection connection = context.mock(Connection.class);
    Application app = context.mock(Application.class);

    States connectionStatus = context.states("connection").startsAs("closed");

    ConnectionManager connectionManager = new ConnectionManager(dataSource);

    Server server = new Server(9999);
    HttpRequest request = aRequest().to(server);

    @Before public void
    startServer() throws Exception {
        context.checking(new Expectations() {{
            oneOf(dataSource).getConnection(); will(returnValue(connection)); when(connectionStatus.is("closed")); then(connectionStatus.is("opened"));
            oneOf(connection).close(); when(connectionStatus.is("opened")); then(connectionStatus.is("closed"));
        }});

        server.run(new MiddlewareStack() {{
            use(connectionManager);
            run(app);
        }});
    }

    @After public void
    stopServer() throws Exception {
        server.shutdown();
    }

    @Test public void
    makesConnectionAvailableToNextApplication() throws Exception {
        context.checking(new Expectations() {{
            oneOf(app).handle(with(aRequestWithAttribute("jdbc.connection", sameConnection(connection))), with(any(Response.class))); when(connectionStatus.is("opened"));
        }});

        request.send();
    }

    @Test public void
    gracefullyClosesConnectionWhenAnErrorOccurs() throws Exception {
        context.checking(new Expectations() {{
            allowing(app).handle(with(any(Request.class)), with(any(Response.class))); will(throwException(new Exception("error")));
        }});

        request.send();
    }

    private Matcher<Object> sameConnection(final Connection connection) {
        return same((Object) connection);
    }

    private Matcher<Request> aRequestWithAttribute(final String attribute, Matcher<Object> connection) {
        return new FeatureMatcher<Request, Object>(connection, "a request with attribute " + attribute, attribute) {
            protected Object featureValueOf(Request actual) {
                return actual.getAttribute(attribute);
            }
        };
    }
}