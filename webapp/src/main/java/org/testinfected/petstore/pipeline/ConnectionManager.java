package org.testinfected.petstore.pipeline;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

import javax.sql.DataSource;
import java.sql.Connection;

public class ConnectionManager extends AbstractMiddleware {

    public static final String JDBC_CONNECTION = "jdbc.connection";

    private final DataSource dataSource;

    public ConnectionManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @SuppressWarnings("unchecked")
    public void handle(Request request, Response response) throws Exception {
        Connection connection = dataSource.getConnection();
        request.getAttributes().put(JDBC_CONNECTION, connection);
        try {
            forward(request, response);
        } finally {
            connection.close();
        }
    }
}
