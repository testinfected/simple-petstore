package org.testinfected.petstore.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Select<T> {

    public static <T> Select<T> from(final Record<T> record) {
        return new Select<T>(record);
    }

    private final Record<T> from;
    private final StringBuilder whereClause = new StringBuilder();
    private final List<Object> parameters = new ArrayList<Object>();

    private Select(final Record<T> from) {
        this.from = from;
    }

    public T single(final Connection connection) {
        return list(connection).get(0);
    }

    public List<T> list(final Connection connection) {
        List<T> entities = new ArrayList<T>();
        PreparedStatement query = null;
        try {
            query = connection.prepareStatement(buildSelectStatement());
            for (int index = 0; index < parameters.size(); index++) {
                setParameter(query, index);
            }
            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                entities.add(from.hydrate(resultSet));
            }
        } catch (SQLException e) {
            throw new JDBCException("Could not execute query", e);
        } finally {
            Sql.close(query);
        }
        return entities;
    }

    private String buildSelectStatement() {
        return "select " + Sql.asString(from.columns()) + " from " + from.table() + " where" + whereClause;
    }

    private void setParameter(PreparedStatement query, int index) throws SQLException {
        int sqlType = query.getParameterMetaData().getParameterType(index + 1);
        query.setObject(index + 1, parameters.get(index), sqlType);
    }

    public void where(String clause, Object... values) {
        whereClause.append(" ").append(clause);
        addParameters(values);
    }

    private void addParameters(Object... values) {
        for (Object value : values) {
            addParameter(value);
        }
    }

    private void addParameter(Object value) {
        parameters.add(value);
    }
}
