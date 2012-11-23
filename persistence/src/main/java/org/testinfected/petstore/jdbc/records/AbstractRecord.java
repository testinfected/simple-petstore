package org.testinfected.petstore.jdbc.records;

import org.testinfected.petstore.jdbc.Properties;
import org.testinfected.petstore.jdbc.support.Record;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractRecord<T> implements Record<T> {

    @Override
    public void handleKeys(ResultSet keys, T entity) throws SQLException {
        Properties.idOf(entity).set(generatedId(keys));
    }

    private long generatedId(ResultSet rs) throws SQLException {
        rs.first();
        return rs.getLong(1);
    }
}
