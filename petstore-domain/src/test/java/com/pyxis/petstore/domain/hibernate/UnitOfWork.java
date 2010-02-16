package com.pyxis.petstore.domain.hibernate;

public interface UnitOfWork {

    void work() throws Exception;
}
