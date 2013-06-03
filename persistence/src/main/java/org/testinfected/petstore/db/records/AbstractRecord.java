package org.testinfected.petstore.db.records;

import org.testinfected.petstore.db.Access;
import org.testinfected.petstore.db.support.Record;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractRecord<T> implements Record<T> {

    @Override
    public void handleKeys(ResultSet keys, T entity) throws SQLException {
        Access.idOf(entity).set(generatedId(keys));
    }

    private long generatedId(ResultSet rs) throws SQLException {
        rs.first();
        return rs.getLong(1);
    }
}
