package org.testinfected.petstore.middlewares;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

import javax.sql.DataSource;
import java.sql.Connection;

public class ConnectionScope extends AbstractMiddleware {

    private final DataSource dataSource;

    public ConnectionScope(DataSource dataSource) {
        this.dataSource = dataSource;
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
            return (Connection) request.getAttribute(Connection.class);
        }

        @SuppressWarnings("unchecked")
        private void set(Connection connection) {
            request.getAttributes().put(Connection.class, connection);
        }

        public void unset() {
            request.getAttributes().remove(Connection.class);
        }
    }
}
