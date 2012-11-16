package org.testinfected.petstore.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class Column {
    private final String name;

    public Column(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setValue(PreparedStatement statement, int index, Object value) throws SQLException {
        statement.setObject(index, value, Types.VARCHAR);
    }
}
