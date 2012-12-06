package org.testinfected.support.middlewares;

import org.testinfected.support.Request;
import org.testinfected.support.Response;
import org.testinfected.support.SimpleRequest;
import org.testinfected.support.SimpleResponse;

import javax.sql.DataSource;
import java.nio.charset.Charset;
import java.sql.Connection;

public class ConnectionScope extends AbstractMiddleware {

    private final DataSource dataSource;

    public ConnectionScope(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void handle(org.simpleframework.http.Request request, org.simpleframework.http.Response response) throws Exception {
        handle(new SimpleRequest(request), new SimpleResponse(response, null, Charset.defaultCharset()));
    }

    public void handle(Request request, Response response) throws Exception {
        Connection connection = dataSource.getConnection();
        ConnectionReference ref = new ConnectionReference(request);

        ref.set(connection);
        try {
            forward(request, response);
        } finally {
            ref.unset();
            connection.close();
        }
    }

    public static class ConnectionReference {

        private final Request request;

        public ConnectionReference(Request request) {
            this.request = request;
        }

        public Connection get() {
            return (Connection) request.attribute(Connection.class);
        }

        private void set(Connection connection) {
            request.attribute(Connection.class, connection);
        }

        public void unset() {
            request.removeAttribute(Connection.class);
        }
    }
}
