package com.pyxis.petstore.persistence;

import com.pyxis.petstore.domain.Item;
import com.pyxis.petstore.domain.ItemCatalog;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class HibernateItemCatalog implements ItemCatalog {

    private final SessionFactory sessionFactory;

    public HibernateItemCatalog(SessionFactory sessionFactory) {
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