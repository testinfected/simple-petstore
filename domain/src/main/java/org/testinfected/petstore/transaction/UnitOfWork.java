package org.testinfected.petstore.transaction;

public interface UnitOfWork {

    void execute() throws Exception;
}
