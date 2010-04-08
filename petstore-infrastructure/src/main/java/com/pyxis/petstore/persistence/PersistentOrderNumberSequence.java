package com.pyxis.petstore.persistence;

import com.pyxis.petstore.Factory;
import com.pyxis.petstore.domain.OrderNumber;
import com.pyxis.petstore.domain.OrderNumberSequence;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Factory
public class PersistentOrderNumberSequence implements OrderNumberSequence {
    private final SessionFactory sessionFactory;

    @Autowired
    public PersistentOrderNumberSequence(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public OrderNumber nextOrderNumber() {
        Session session = sessionFactory.getCurrentSession();
        session.createSQLQuery("insert into order_number_sequence values(null)").executeUpdate();
        Long nextValue = (Long) session.createSQLQuery("select @@identity").
                addScalar("@@identity", Hibernate.LONG).
                uniqueResult();
        return new OrderNumber(nextValue);
    }
}
