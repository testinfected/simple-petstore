package org.testinfected.petstore;

public interface UnitOfWork {

    void execute() throws Exception;
}
