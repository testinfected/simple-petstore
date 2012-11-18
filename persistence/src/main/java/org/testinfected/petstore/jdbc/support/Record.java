package org.testinfected.petstore.jdbc.support;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface Record<T> {

    String table();

    List<String> columns();

    T hydrate(ResultSet rs) throws SQLException;

    void dehydrate(PreparedStatement statement, T entity) throws SQLException;
}
