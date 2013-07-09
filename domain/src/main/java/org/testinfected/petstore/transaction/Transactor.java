package org.testinfected.petstore.transaction;

public interface Transactor {

    void perform(UnitOfWork work) throws Exception;

    <T> T performQuery(QueryUnitOfWork<T> query) throws Exception;
}
