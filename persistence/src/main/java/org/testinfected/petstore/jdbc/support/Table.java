package org.testinfected.petstore.jdbc.support;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Table<T> {

    private final String name;
    private final Record<T> record;
    private final List<Column<?>> columns = new ArrayList<Column<?>>();

    public Table(String name, Record<T> record) {
        this.name = name;
        this.record = record;
    }

    public String name() {
        return name;
    }

    public Column<Long> LONG(String name) {
        return add(new Column<Long>(this, name, Types.LONG));
    }

    public Column<String> STRING(String name) {
        return add(new Column<String>(this, name, Types.STRING));
    }

    public Column<BigDecimal> BIG_DECIMAL(String name) {
        return add(new Column<BigDecimal>(this, name, Types.BIG_DECIMAL));
    }

    public Column<Integer> INT(String name) {
        return add(new Column<Integer>(this, name, Types.INT));
    }

    public <T> Column<T> add(Column<T> column) {
        columns.add(column);
        return column;
    }

    public List<String> columnNames() {
        List<String> names = new ArrayList<String>();
        for (Column<?> column : columns) {
            names.add(column.name());
        }
        return names;
    }

    public int indexOf(Column<?> column) {
        return columns.indexOf(column) + 1;
    }

    public T hydrate(ResultSet resultSet) throws SQLException {
        return record.hydrate(resultSet);
    }

    public void dehydrate(PreparedStatement statement, T entity) throws SQLException {
        record.dehydrate(statement, entity);
    }

    public void handleKeys(ResultSet keys, T entity) throws SQLException {
        record.handleKeys(keys, entity);
    }
}
