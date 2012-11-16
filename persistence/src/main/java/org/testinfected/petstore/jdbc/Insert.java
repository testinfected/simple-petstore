package org.testinfected.petstore.jdbc;

import com.pyxis.petstore.domain.product.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class Insert {
    private final Table table;
    private final Product product;

    public static Insert into(Table table, Product product) {
        return new Insert(table, product);
    }

    public Insert(Table table, final Product product) {
        this.table = table;
        this.product = product;
    }

    public void execute(final Connection connection) {
        PreparedStatement insert = null;
        try {
            insert = connection.prepareStatement(insertStatementFor(table), RETURN_GENERATED_KEYS);
            table.writeRecord(insert, product);
            executeInsert(insert);
            Properties.idOf(product).set(generatedIdOf(insert));
        } catch (SQLException e) {
            throw new JDBCException("Could not insert product " + product, e);
        } finally {
            close(insert);
        }
    }

    private String insertStatementFor(final Table table) {
        StringBuilder sql = new StringBuilder();
        sql.append("insert into ").append(table.getName());
        sql.append("(").append(asString(table.columnNames())).append(")");
        sql.append(" values(").append(asString(parametersFor(table.columnNames()))).append(")");
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

    private String asString(Iterable<?> elements) {
        StringBuilder builder = new StringBuilder();
        for (Iterator<?> it = elements.iterator(); it.hasNext(); ) {
            builder.append(it.next());
            if (it.hasNext()) builder.append(", ");
        }
        return builder.toString();
    }

    private void close(Statement statement) {
        if (statement == null) return;
        try {
            statement.close();
        } catch (SQLException ignored) {
        }
    }
}
