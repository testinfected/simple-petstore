package org.testinfected.petstore.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class Insert<T> {
    private final Table<T> table;
    private final T entity;

    public static <T> Insert<T> into(Table<T> table, T entity) {
        return new Insert<T>(table, entity);
    }

    public Insert(Table<T> table, final T entity) {
        this.table = table;
        this.entity = entity;
    }

    public void execute(final Connection connection) {
        PreparedStatement insert = null;
        try {
            insert = connection.prepareStatement(insertStatementFor(table), RETURN_GENERATED_KEYS);
            table.writeRecord(insert, entity);
            executeInsert(insert);
            Properties.idOf(entity).set(generatedIdOf(insert));
        } catch (SQLException e) {
            throw new JDBCException("Could not insert product " + entity, e);
        } finally {
            Sql.close(insert);
        }
    }

    private String insertStatementFor(final Table<T> table) {
        StringBuilder sql = new StringBuilder();
        sql.append("insert into ").append(table.getName());
        sql.append("(").append(Sql.asString(table.columnNames())).append(")");
        sql.append(" values(").append(Sql.asString(parametersFor(table.columnNames()))).append(")");
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

    private long generatedIdOf(PreparedStatement insert) throws SQLException {
        ResultSet generatedKeys = insert.getGeneratedKeys();
        generatedKeys.first();
        return generatedKeys.getLong(1);
    }
}
