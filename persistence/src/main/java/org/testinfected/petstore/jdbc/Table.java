package org.testinfected.petstore.jdbc;

import com.pyxis.petstore.domain.product.Product;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
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

    public List<Column> columns() {
        return new ArrayList<Column>(columns.values());
    }

    public int columnCount() {
        return columns.size();
    }

    public Column column(int index) {
        return columns().get(index - 1);
    }

    public Iterable<String> columnNames() {
        List<String> names = new ArrayList<String>();
        for (Column column : columns.values()) {
            names.add(column.getName());
        }
        return names;
    }

    public void writeRecord(PreparedStatement statement, Product product) throws SQLException {
        Map<Column, Object> row = asRow(record.dehydrate(product));

        for (int index = 1; index <= columnCount(); index++) {
            statement.setObject(index, row.get(column(index)), Types.VARCHAR);
        }
    }

    private Map<Column, Object> asRow(Map<String, Object> values) {
        Map<Column, Object> row = new HashMap<Column, Object>();
        for (Map.Entry<String, Column> entry : columns.entrySet()) {
            row.put(entry.getValue(), values.get(entry.getKey()));
        }
        return row;
    }
}
