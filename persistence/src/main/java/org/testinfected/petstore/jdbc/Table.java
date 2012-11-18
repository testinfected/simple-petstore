package org.testinfected.petstore.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Table<T> {
    private final String name;
    private final List<Column> columns = new ArrayList<Column>();
    private final Record<T> record;

    public Table(String name, Record<T> record) {
        this.name = name;
        this.record = record;
    }

    public String getName() {
        return name;
    }

    public void addColumn(Column column) {
        columns.add(column);
    }

    public Iterable<String> columnNames() {
        List<String> names = new ArrayList<String>();
        for (Column column : columns) {
            names.add(column.getName());
        }
        return names;
    }

    public T readRecord(ResultSet resultSet) throws SQLException {
        Row row = new Row(name, columns);
        row.readFrom(resultSet);
        return record.hydrate(row);
    }

    public void writeRecord(PreparedStatement statement, T entity) throws SQLException {
        Row row = new Row(name, columns);
        record.dehydrate(row, entity);
        row.writeTo(statement);
    }
}
