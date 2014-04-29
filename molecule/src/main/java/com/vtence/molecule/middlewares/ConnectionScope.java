package com.vtence.molecule.middlewares;

import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import com.vtence.molecule.lib.AbstractMiddleware;

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
        Reference ref = new Reference(request);

        ref.set(connection);
        try {
            forward(request, response);
        } finally {
            ref.unset();
            close(connection);
        }
    }

    private void close(Connection connection) throws SQLException {
        try { connection.close(); } catch (SQLException ignored) {}
    }

    public static class Reference {
        private final Request request;

        public Reference(Request request) {
            this.request = request;
        }

        public Connection get() {
            return request.attribute(Connection.class);
        }

        private void set(Connection connection) {
            request.attribute(Connection.class, connection);
        }

        public void unset() {
            request.removeAttribute(Connection.class);
        }
    }
}
