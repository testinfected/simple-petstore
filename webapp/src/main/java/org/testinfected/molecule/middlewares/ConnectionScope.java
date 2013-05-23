package org.testinfected.molecule.middlewares;

import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

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
            close(connection);
        }
    }

    private void close(Connection connection) {
        try { connection.close(); } catch (SQLException ignored) {}
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
