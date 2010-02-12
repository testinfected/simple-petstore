package com.pyxis.petstore.domain.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.pyxis.petstore.domain.Item;
import com.pyxis.petstore.domain.ItemRepository;

public class HibernateItemRepository implements ItemRepository {

    private HibernateTemplate hibernateTemplate;

    public HibernateItemRepository(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

    @SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Item> findItemsByQuery(String query) {
		DetachedCriteria searchCriteria = DetachedCriteria.forClass(Item.class);
    	searchCriteria.add(Restrictions.like("name", query, MatchMode.ANYWHERE));
    	return this.hibernateTemplate.findByCriteria(searchCriteria);
	}

}
