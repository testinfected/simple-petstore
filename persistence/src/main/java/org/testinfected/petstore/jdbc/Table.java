package org.testinfected.petstore.jdbc;

import com.pyxis.petstore.domain.product.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Table {
    private final String name;
    private final Map<String, Column> columns = new LinkedHashMap<String, Column>();
    private final ProductRecord record = new ProductRecord();

    public Table(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addColumn(Column column) {
        addColumn(column.getName(), column);
    }

    public void addColumn(String key, Column column) {
        columns.put(key, column);
    }

    public Iterable<String> columnNames() {
        List<String> names = new ArrayList<String>();
        for (Column column : columns.values()) {
            names.add(column.getName());
        }
        return names;
    }

    public Product readRecord(ResultSet resultSet) throws SQLException {
        Row row = new Row(name, columns);
        row.readFrom(resultSet);
        return record.hydrate(row);
    }

    public void writeRecord(PreparedStatement statement, Product product) throws SQLException {
        Row row = new Row(name, columns);
        record.dehydrate(row, product);
        row.writeTo(statement);
    }
}
