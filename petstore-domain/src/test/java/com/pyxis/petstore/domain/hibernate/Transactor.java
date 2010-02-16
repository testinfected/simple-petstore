package com.pyxis.petstore.domain.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class Transactor {

    private final Session session;

    public Transactor(Session session) {
        this.session = session;
    }

    public void perform(UnitOfWork unitOfWork) throws Exception {
        Transaction transaction = session.beginTransaction();
        try {
            unitOfWork.work();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }
}
