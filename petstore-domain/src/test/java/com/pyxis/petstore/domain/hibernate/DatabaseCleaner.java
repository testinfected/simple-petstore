package com.pyxis.petstore.domain.hibernate;

import com.pyxis.petstore.domain.Item;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DatabaseCleaner {

    private static final Class<?>[] ENTITY_TYPES = {
        Item.class
    };

    private final Session session;

    public DatabaseCleaner(Session session) {
        this.session = session;
    }

    public void clean() {
      Transaction transaction = session.beginTransaction();
      for (Class<?> entityType : ENTITY_TYPES) {
        deleteEntities(entityType);
      }
      transaction.commit();
    }

    private void deleteEntities(Class<?> entityType) {
        session.createQuery("delete from " + entityNameOf(entityType)).executeUpdate();
    }

    private String entityNameOf(Class<?> entityType) {
        return entityType.getName();
    }
}
