package org.testinfected.petstore.jdbc;

public interface UnitOfWork {

    void execute() throws Exception;
}
