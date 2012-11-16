package org.testinfected.petstore.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class Column {
    public static Column varchar(String name) {
        return new Column(name, Types.VARCHAR);
    }

    public static Column bigint(String name) {
        return new Column(name, Types.BIGINT);
    }

    private final String name;
    private final int sqlType;

    public Column(String name, int sqlType) {
        this.name = name;
        this.sqlType = sqlType;
    }

    public String getName() {
        return name;
    }

    public void setValue(PreparedStatement statement, int index, Object value) throws SQLException {
        statement.setObject(index, value, sqlType);
    }

    public Object getValue(ResultSet resultSet, int index) throws SQLException {
        return resultSet.getObject(index);
    }
}
