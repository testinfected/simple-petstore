package org.testinfected.petstore;

public interface Transactor {

    void perform(UnitOfWork work) throws Exception;
}
