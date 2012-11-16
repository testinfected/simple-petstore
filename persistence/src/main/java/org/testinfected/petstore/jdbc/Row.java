package org.testinfected.petstore.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Row {

    private final String tableName;
    private final Map<String, Column> columns;
    private final Map<String, Object> values = new HashMap<String, Object>();

    public Row(final String tableName, Map<String, Column> columns) {
        this.columns = columns;
        this.tableName = tableName;
    }

    public void setValue(String columnKey, Object value) {
        values.put(columnKey, value);
    }

    private List<Column> listColumns() {
        return new ArrayList<Column>(columns.values());
    }

    private int columnCount() {
        return columns.size();
    }

    private Column columnAt(int index) {
        return listColumns().get(index - 1);
    }

    public void writeTo(PreparedStatement statement) throws SQLException {
        final Map<String, ?> byColumn = sortByColumns(values);

        for (int index = 1; index <= columnCount(); index++) {
            Column column = columnAt(index);
            column.setValue(statement, index, byColumn.get(column.getName()));
        }
    }

    private Map<String, ?> sortByColumns(final Map<String, Object> values) {
        Map<String, Object> mapping = new HashMap<String, Object>();
        for (String key : columns.keySet()) {
            Column column = columns.get(key);
            mapping.put(column.getName(), values.get(key));
        }
        return mapping;
    }

    public void readFrom(ResultSet resultSet) throws SQLException {
        for (String key : columns.keySet()) {
            Column column = columns.get(key);
            int index = findColumnIndex(resultSet, column.getName());
            values.put(key, column.getValue(resultSet, index));
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
