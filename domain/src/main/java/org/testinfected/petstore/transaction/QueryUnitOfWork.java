package org.testinfected.petstore.transaction;

public abstract class QueryUnitOfWork<T> implements UnitOfWork {

    public T result;

    public void execute() throws Exception {
        result = query();
    }

    public abstract T query() throws Exception;
}
