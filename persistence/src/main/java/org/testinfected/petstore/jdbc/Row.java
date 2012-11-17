package org.testinfected.petstore.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public class Row {

    private final String tableName;
    private final List<Column> columns = new ArrayList<Column>();
    private final Map<String, Object> values = new HashMap<String, Object>();

    public Row(final String tableName, Collection<Column> columns) {
        this.columns.addAll(columns);
        this.tableName = tableName;
    }

    public void setValue(String name, Object value) {
        values.put(name, value);
    }

    private int columnCount() {
        return columns.size();
    }

    private Column columnAt(int index) {
        return columns.get(index - 1);
    }

    public void writeTo(PreparedStatement statement) throws SQLException {
        for (int index = 1; index <= columnCount(); index++) {
            Column column = columnAt(index);
            column.setValue(statement, index, values.get(column.getName()));
        }
    }

    public void readFrom(ResultSet resultSet) throws SQLException {
        for (Column column : columns) {
            int index = findColumnIndex(resultSet, column.getName());
            values.put(column.getName(), column.getValue(resultSet, index));
        }
    }

    public String getString(String key) throws SQLException {
        return getValue(key, String.class);
    }

    public long getLong(String key) throws SQLException {
        return getValue(key, Long.class);
    }

    public <T> T getValue(String key, Class<T> type) {
        return type.cast(values.get(key));
    }

    private int findColumnIndex(ResultSet resultSet, final String columnName) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        for (int index = 1; index <= columnCount; index++) {
            if (metaData.getColumnName(index).equals(columnName) && metaData.getTableName(index).equals(tableName))
                return index;
        }

        throw new SQLException("Result set has no column '" + columnName + "'");
    }
}
