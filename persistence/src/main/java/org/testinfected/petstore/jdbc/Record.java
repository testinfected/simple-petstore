package org.testinfected.petstore.jdbc;

import java.sql.SQLException;

public interface Record<T> {

    T hydrate(Row row) throws SQLException;

    void dehydrate(Row row, T entity) throws SQLException;
}
