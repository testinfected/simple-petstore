package com.pyxis.petstore.persistence;

import com.pyxis.petstore.domain.Item;
import com.pyxis.petstore.domain.ItemInventory;
import com.pyxis.petstore.domain.ItemNumber;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hibernate.criterion.Restrictions.eq;

@Repository 
public class PersistentItemInventory implements ItemInventory {

	private final SessionFactory sessionFactory;

    @Autowired
	public PersistentItemInventory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Item> findByProductNumber(String productNumber) {
		return currentSession().createCriteria(Item.class).
                createCriteria("product").
                add(eq("number", productNumber)).
                list();
	}

    @Transactional(readOnly = true)
    public Item find(ItemNumber itemNumber) {
        return (Item) currentSession().createCriteria(Item.class).
                add(eq("number", itemNumber)).
                uniqueResult();
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
}
