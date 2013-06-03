package org.testinfected.petstore.db.support;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class Column<T> {

    private final Table<?> table;
    private final String name;
    private final Type<T> type;

    public Column(Table<?> table, String name, Type<T> type) {
        this.table = table;
        this.name = name;
        this.type = type;
    }

    public String name() {
        return name;
    }

    public void set(PreparedStatement statement, T value) throws SQLException {
        type.set(statement, table.indexOf(this), value);
    }

    public T get(ResultSet resultSet) throws SQLException {
        return type.get(resultSet, indexIn(resultSet));
    }

    private int indexIn(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();

        for (int index = 1; index <= metaData.getColumnCount(); index++) {
            if (metaData.getColumnName(index).equals(name) && metaData.getTableName(index).equals(table.name()))
                return index;
        }

        throw new SQLException("Result set has no column '" + name + "'");
    }

    public static interface Type<T> {

        T get(ResultSet rs, int index) throws SQLException;

        void set(PreparedStatement statement, int index, T value) throws SQLException;
    }
}
