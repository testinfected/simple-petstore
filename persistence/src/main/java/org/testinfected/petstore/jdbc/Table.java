package org.testinfected.petstore.jdbc;

import java.util.*;

public class Table {
    private final String name;
    private final Map<String, Column> columns = new LinkedHashMap<String, Column>();

    public Table(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Iterator<String> columnNames() {
        List<String> columnNames = new ArrayList<String>();
        for (Column column : columns.values()) {
            columnNames.add(column.getName());
        }
        return columnNames.iterator();
    }

    public Iterator<Column> columns() {
        return columns.values().iterator();
    }

    public void addColumn(Column column) {
        addColumn(column.getName(), column);
    }

    public void addColumn(String name, Column column) {
        columns.put(name, column);
    }
}
