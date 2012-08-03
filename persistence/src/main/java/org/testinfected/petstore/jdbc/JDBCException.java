package org.testinfected.petstore.jdbc;

public class JDBCException extends RuntimeException {

    public JDBCException(String message, Throwable cause) {
        super(message, cause);
    }
}
