package org.testinfected.petstore.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Record<T> {

    T hydrate(ResultSet resultSet) throws SQLException;
}
