package org.testinfected.petstore.db;

import com.vtence.tape.JDBC;
import com.vtence.tape.JDBCException;
import org.testinfected.petstore.transaction.AbstractTransactor;
import org.testinfected.petstore.transaction.UnitOfWork;

import java.sql.Connection;
import java.sql.SQLException;

public class JDBCTransactor extends AbstractTransactor {
    private final Connection connection;

    public JDBCTransactor(Connection connection) {
        this.connection = connection;
    }

    public void perform(UnitOfWork unitOfWork) throws Exception {
        boolean autoCommit = connection.getAutoCommit();
        
        try {
            connection.setAutoCommit(false);
            unitOfWork.execute();
            connection.commit();
        } catch (SQLException e) {
            JDBC.rollback(connection);
            throw new JDBCException("Transaction failed", e);
        } catch (Exception e) {
            JDBC.rollback(connection);
            throw e;
        } finally {
            resetAutoCommitTo(autoCommit);
        }
    }

    private void resetAutoCommitTo(boolean autoCommit) {
        try {
            connection.setAutoCommit(autoCommit);
        } catch (SQLException ignored) {
        }
    }
}
