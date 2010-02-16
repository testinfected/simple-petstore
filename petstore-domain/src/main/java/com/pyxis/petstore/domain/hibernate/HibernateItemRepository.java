package com.pyxis.petstore.domain.hibernate;

import com.pyxis.petstore.domain.Item;
import com.pyxis.petstore.domain.ItemRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class HibernateItemRepository implements ItemRepository {

    private SessionFactory sessionFactory;

    public HibernateItemRepository(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

    @SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Item> findItemsByKeyword(String keyword) {
        Session session = sessionFactory.getCurrentSession();
        return session.createCriteria(Item.class)
    	    .add(Restrictions.like("name", keyword, MatchMode.ANYWHERE))
    	    .list();
	}
}