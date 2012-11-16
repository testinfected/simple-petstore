package org.testinfected.petstore.jdbc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class Table {
    private final String name;
    private final Collection<String> columns = new ArrayList<String>();

    public Table(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Iterator<String> getColumns() {
        return columns.iterator();
    }

    public void addColumns(String... names) {
        columns.addAll(Arrays.asList(names));
    }
}
