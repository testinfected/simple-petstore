package com.pyxis.petstore.persistence;

import com.pyxis.petstore.domain.order.Order;
import com.pyxis.petstore.domain.order.OrderBook;
import com.pyxis.petstore.domain.order.OrderNumber;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static org.hibernate.criterion.Restrictions.eq;

@Repository
public class PersistentOrderBook implements OrderBook {

	private final SessionFactory sessionFactory;

    @Autowired
	public PersistentOrderBook(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

    @Transactional(readOnly = true)
    public Order find(OrderNumber orderNumber) {
        return (Order) currentSession().createCriteria(Order.class).
                add(eq("number", orderNumber)).
                uniqueResult();
    }

    @Transactional
    public void record(Order order) {
        currentSession().saveOrUpdate(order);
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
}