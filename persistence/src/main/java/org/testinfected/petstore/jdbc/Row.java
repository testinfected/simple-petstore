package org.testinfected.petstore.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Row {

    private final Map<String, Column> columns;
    private final Map<String, Object> values = new HashMap<String, Object>();

    public Row(Map<String, Column> columns) {
        this.columns = columns;
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

    public void writeValues(PreparedStatement statement) throws SQLException {
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
}
