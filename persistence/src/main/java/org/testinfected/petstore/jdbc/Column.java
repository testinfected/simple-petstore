package org.testinfected.petstore.jdbc;

public class Column {
    private final String name;

    public Column(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
