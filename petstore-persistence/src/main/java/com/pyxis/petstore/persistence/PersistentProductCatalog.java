package com.pyxis.petstore.persistence;

import com.pyxis.petstore.domain.Product;
import com.pyxis.petstore.domain.ProductCatalog;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class PersistentProductCatalog implements ProductCatalog {

    private final SessionFactory sessionFactory;

    public PersistentProductCatalog(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

    @SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Product> findProductsByKeyword(String keyword) {
        Session session = sessionFactory.getCurrentSession();
        return session.createCriteria(Product.class)
    	    .add(Restrictions.ilike("name", keyword, MatchMode.ANYWHERE))
    	    .list();
	}
}