package com.pyxis.petstore.persistence;

import java.util.List;

import com.pyxis.petstore.domain.ItemInventory;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.transaction.annotation.Transactional;

import com.pyxis.petstore.domain.Item;

public class PersistentItemInventory implements ItemInventory {

	private SessionFactory sessionFactory;

	public PersistentItemInventory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Item> findByProductNumber(String productNumber) {
		return currentSession().createQuery("from Item where product.number = :pnumber").
                setString("pnumber", productNumber)
                .list();
//		criteria.createCriteria("product").add(eq("number", productNumber));
//		return criteria.list();
	}

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
}
