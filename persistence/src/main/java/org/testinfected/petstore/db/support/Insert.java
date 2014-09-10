package org.testinfected.petstore.db.support;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class Insert<T> {
    private final Table<T> into;
    private final T entity;

    public static <T> Insert<T> into(Table<T> table, T entity) {
        return new Insert<T>(table, entity);
    }

    public Insert(Table<T> table, final T entity) {
        this.into = table;
        this.entity = entity;
    }

    public void execute(final Connection connection) {
        PreparedStatement insert = null;
        try {
            insert = connection.prepareStatement(buildInsertStatement(), RETURN_GENERATED_KEYS);
            into.dehydrate(insert, entity);
            executeInsert(insert);
            into.handleKeys(insert.getGeneratedKeys(), entity);
        } catch (SQLException e) {
            throw new JDBCException("Could not insert entity " + entity, e);
        } finally {
            JDBC.close(insert);
        }
    }

    private String buildInsertStatement() {
        StringBuilder sql = new StringBuilder();
        sql.append("insert into ").append(into.name());
        sql.append("(").append(JDBC.asString(into.columns())).append(")");
        sql.append(" values(").append(JDBC.asString(parametersFor(into.columns()))).append(")");
        return sql.toString();
    }

    private List<String> parametersFor(final Iterable<String> columns) {
        List<String> parameters = new ArrayList<String>();
        for (String ignored : columns){
            parameters.add("?");
        }
        return parameters;
    }

    private void executeInsert(PreparedStatement insert) throws SQLException {
        int rowsInserted = insert.executeUpdate();
        if (rowsInserted != 1) {
            throw new SQLException("Unexpected row count of " + rowsInserted + "; expected was 1");
        }
    }
}
