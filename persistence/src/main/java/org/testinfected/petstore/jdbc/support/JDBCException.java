package org.testinfected.petstore.jdbc.support;

public class JDBCException extends RuntimeException {

    public JDBCException(String message, Throwable cause) {
        super(message, cause);
    }
}
