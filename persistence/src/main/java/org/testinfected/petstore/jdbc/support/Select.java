package org.testinfected.petstore.jdbc.support;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Select<T> {

    public static <T> Select<T> from(final Table<T> table) {
        return from(table, table.name());
    }

    public static <T> Select<T> from(final Table<T> table, String alias) {
        return new Select<T>(table, alias);
    }

    private final Table<T> from;

    private final List<Table<?>> joins = new ArrayList<Table<?>>();
    private final StringBuilder joinClause = new StringBuilder();
    private final StringBuilder whereClause = new StringBuilder();
    private final List<Object> parameters = new ArrayList<Object>();
    private final StringBuilder orderByClause = new StringBuilder();
    private final Map<String, String> aliases = new HashMap<String, String>();

    public Select(Table<T> from, String alias) {
        this.from = from;
        aliasTable(from.name(), alias);
    }

    private void aliasTable(final String table, String alias) {
        aliases.put(table, alias);
    }

    public Select<T> join(Table<?> join, String alias, String clause) {
        return withJoin(join, alias, "inner join", clause);
    }

    public Select<T> leftJoin(Table<?> join, String alias, String clause) {
        return withJoin(join, alias, "left outer join", clause);
    }

    private Select<T> withJoin(Table<?> join, String alias, String joinType, String clause) {
        joins.add(join);
        aliasTable(join.name(), alias);
        joinClause.append(" ").append(joinType).append(" ").append(join.name()).append(" ").append(aliasOf(join)).append(" on ").append(clause);
        return this;
    }

    public Select<T> where(String clause, Object... values) {
        whereClause.append(" where ").append(clause);
        addParameters(values);
        return this;
    }

    public Select<T> orderBy(String clause) {
        orderByClause.append(" order by ").append(clause);
        return this;
    }

    public T first(final Connection connection) {
        List<T> entities = list(connection);
        return entities.isEmpty() ? null : entities.get(0);
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
        StringBuilder sql = new StringBuilder();
        sql.append(selectClause());
        sql.append(fromClause());
        sql.append(joinClause);
        sql.append(whereClause);
        sql.append(orderByClause);
        return sql.toString();
    }

    private String fromClause() {
        return " from " + from.name() + " " + aliasOf(from);
    }

    private String selectClause() {
        return "select " + Sql.asString(listColumns());
    }

    private Collection<String> listColumns() {
        Collection<String> names = new ArrayList<String>();
        names.addAll(columnsFor(from));
        for (Table<?> join : joins) {
            names.addAll(columnsFor(join));
        }
        return names;
    }

    private String aliasOf(final Table<?> table) {
        return aliases.get(table.name());
    }

    public List<String> columnsFor(Table<?> table) {
        List<String> columns = new ArrayList<String>();
        for (String column : table.columnNames()) {
            columns.add(aliasOf(table) + "." + column);
        }
        return columns;
    }

    private void setParameter(PreparedStatement query, int index) throws SQLException {
        int sqlType = query.getParameterMetaData().getParameterType(index + 1);
        query.setObject(index + 1, parameters.get(index), sqlType);
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