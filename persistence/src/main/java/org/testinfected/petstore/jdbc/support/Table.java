package org.testinfected.petstore.jdbc.support;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Table {

    public static Table named(String name) {
        return new Table(name);
    }

    private final String name;
    private final List<Column<?>> columns = new ArrayList<Column<?>>();

    public Table(String name) {
        this.name = name;
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
}
