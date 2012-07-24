package org.testinfected.petstore.jdbc;

public interface Transactor {

    void perform(UnitOfWork work) throws Exception;
}
