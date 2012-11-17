package org.testinfected.petstore.jdbc;

import com.pyxis.petstore.domain.product.Product;

import java.sql.*;
import java.util.*;

public class Select {

    public static Select from(final Table table) {
        return new Select(table);
    }

    private final Table from;
    private final StringBuilder whereClause = new StringBuilder();
    private final List<Object> parameters = new ArrayList<Object>();

    private Select(final Table from) {
        this.from = from;
    }

    public Product single(final Connection connection) {
        return list(connection).get(0);
    }

    public List<Product> list(final Connection connection) {
        List<Product> products = new ArrayList<Product>();
        PreparedStatement query = null;
        try {
            query = connection.prepareStatement(selectStatementFor(from));
            for (int index = 0; index < parameters.size(); index++) {
                setParameter(query, index);
            }
            ResultSet resultSet = query.executeQuery();
            while (resultSet.next()) {
                products.add(from.readRecord(resultSet));
            }
        } catch (SQLException e) {
            throw new JDBCException("Could not execute query", e);
        } finally {
            Sql.close(query);
        }
        return products;
    }

    private String selectStatementFor(final Table table) {
        return "select " + Sql.asString(table.columnNames()) + " from " + from.getName() + " where " + whereClause;
    }

    private void setParameter(PreparedStatement query, int index) throws SQLException {
        int sqlType = query.getParameterMetaData().getParameterType(index + 1);
        query.setObject(index + 1, parameters.get(index), sqlType);
    }

    public void where(String columnName, Object value) {
        where(columnName + " = ?");
        addParameter(value);
    }

    public void where(String clause) {
        whereClause.append(" ").append(clause);
    }

    public void addParameter(Object value) {
        parameters.add(value);
    }

    public void or(String clause) {
        where("or " + clause);
    }
}
