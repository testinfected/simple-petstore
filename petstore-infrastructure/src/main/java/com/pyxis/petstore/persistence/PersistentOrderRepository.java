package com.pyxis.petstore.persistence;

import com.pyxis.petstore.domain.order.Order;
import com.pyxis.petstore.domain.order.OrderRepository;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class PersistentOrderRepository implements OrderRepository {

	private final SessionFactory sessionFactory;

    @Autowired
	public PersistentOrderRepository(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

    @Transactional
    public void store(Order order) {
        sessionFactory.getCurrentSession().saveOrUpdate(order);
    }
}