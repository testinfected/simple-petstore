package com.pyxis.petstore.persistence;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.transaction.annotation.Transactional;

import com.pyxis.petstore.domain.Item;
import com.pyxis.petstore.domain.ItemRepository;

public class PersistentItemRepository implements ItemRepository {

	private SessionFactory sessionFactory;

	public PersistentItemRepository(SessionFactory sessionFactory) {
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
