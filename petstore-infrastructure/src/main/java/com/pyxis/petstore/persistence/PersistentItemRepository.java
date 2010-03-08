package com.pyxis.petstore.persistence;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import static org.hibernate.criterion.Restrictions.*;

import com.pyxis.petstore.domain.Item;
import com.pyxis.petstore.domain.ItemRepository;

// TODO use query instead ?
public class PersistentItemRepository implements ItemRepository {

	private SessionFactory sessionFactory;

	public PersistentItemRepository(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Item> findByProductNumber(String productNumber) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Item.class);
		criteria.createCriteria("product").add(eq("number", productNumber));
		return criteria.list();
	}
}
