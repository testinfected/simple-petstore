package org.testinfected.petstore.jdbc;

import com.pyxis.petstore.domain.product.Product;

import java.sql.*;
import java.util.Iterator;

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
            insert = connection.prepareStatement(buildSql(), RETURN_GENERATED_KEYS);
            insert.setObject(1, product.getNumber(), Types.VARCHAR);
            insert.setObject(2, product.getName(), Types.VARCHAR);
            insert.setObject(3, product.getDescription(), Types.VARCHAR);
            insert.setObject(4, product.hasPhoto() ? product.getPhotoFileName() : null, Types.VARCHAR);
            execute(insert);
            ResultSet generatedKeys = insert.getGeneratedKeys();
            generatedKeys.first();
            Properties.idOf(product).set(generatedKeys.getLong(1));
        } catch (SQLException e) {
            throw new JDBCException("Could not insert product " + product, e);
        } finally {
            close(insert);
        }
    }

    private String buildSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("insert into ");
        sql.append(table.getName());
        sql.append("(");
        for (Iterator<String> columns = table.columnNames(); columns.hasNext(); ) {
            String column = columns.next();
            sql.append(column);
            if (columns.hasNext()) sql.append(", ");
        }
        sql.append(") values(");
        for (Iterator<Column> columns = table.columns(); columns.hasNext(); ) {
            columns.next();
            sql.append("?");
            if (columns.hasNext()) sql.append(", ");
        }
        sql.append(")");
        return sql.toString();
    }

    private void execute(PreparedStatement insert) throws SQLException {
        int rowsInserted = insert.executeUpdate();
        if (rowsInserted != 1) {
            throw new SQLException("Unexpected row count of " + rowsInserted + "; expected was 1");
        }
    }

    private void close(Statement statement) {
        if (statement == null) return;
        try {
            statement.close();
        } catch (SQLException ignored) {
        }
    }
}
