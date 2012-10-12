package org.testinfected.petstore.pipeline;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

import javax.sql.DataSource;
import java.sql.Connection;

public class ConnectionManager extends AbstractMiddleware {

    private final DataSource dataSource;

    public ConnectionManager(DataSource dataSource) {
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

    public static Connection get(Request request) {
        return new ConnectionReference(request).get();
    }

    private static class ConnectionReference {
        public static final String KEY = "jdbc.connection";

        private final Request request;

        public ConnectionReference(Request request) {
            this.request = request;
        }

        public Connection get() {
            return (Connection) request.getAttribute(KEY);
        }

        @SuppressWarnings("unchecked")
        private void set(Connection connection) {
            request.getAttributes().put(KEY, connection);
        }

        public void unset() {
            request.getAttributes().remove(KEY);
        }
    }
}
