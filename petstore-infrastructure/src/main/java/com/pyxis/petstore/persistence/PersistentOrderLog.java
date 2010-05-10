package com.pyxis.petstore.persistence;

import com.pyxis.petstore.domain.Maybe;
import com.pyxis.petstore.domain.order.Order;
import com.pyxis.petstore.domain.order.OrderLog;
import com.pyxis.petstore.domain.order.OrderNumber;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.pyxis.petstore.domain.Maybe.maybe;
import static org.hibernate.criterion.Restrictions.eq;

@Repository
public class PersistentOrderLog implements OrderLog {

	private final SessionFactory sessionFactory;

    @Autowired
	public PersistentOrderLog(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

    @Transactional(readOnly = true)
    public Maybe<Order> find(OrderNumber orderNumber) {
        return maybe((Order) currentSession().createCriteria(Order.class).
                add(eq("number", orderNumber)).
                uniqueResult());
    }

    @Transactional
    public void record(Order order) {
        currentSession().saveOrUpdate(order);
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
}