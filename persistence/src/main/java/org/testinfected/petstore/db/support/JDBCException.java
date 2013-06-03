package org.testinfected.petstore.db.support;

import java.sql.SQLException;

public class JDBCException extends RuntimeException {

    public JDBCException(String message, SQLException cause) {
        super(message, cause);
    }

    @Override
    public SQLException getCause() {
        return (SQLException) super.getCause();
    }

    public boolean causedBy(Class<? extends SQLException> sqlException) {
        return sqlException.isInstance(getCause());
    }
}
