package test.support.com.pyxis.petstore.db;

import com.pyxis.petstore.domain.billing.PaymentMethod;
import com.pyxis.petstore.domain.order.LineItem;
import com.pyxis.petstore.domain.order.Order;
import com.pyxis.petstore.domain.product.Item;
import com.pyxis.petstore.domain.product.Product;
import org.hibernate.Session;

public class DatabaseCleaner {

    private static final Class<?>[] ENTITY_TYPES = {
            LineItem.class,
            Order.class,
            PaymentMethod.class,
            Item.class,
            Product.class
    };
    private static final String[] SEQUENCE_NAMES = {
            "order_numbers"
    };

    private final Database database;

    public DatabaseCleaner(Database database) {
        this.database = database;
    }

    public void clean() {
        database.perform(new UnitOfWork() {
            public void work(Session session) {
                clear(session);
                deleteEntities(session);
                resetSequences(session);
            }
        });
    }

    private void clear(Session session) {
        session.clear();
    }

    private void resetSequences(Session session) {
        for (String sequenceName : SEQUENCE_NAMES) {
            session.createSQLQuery("truncate " + sequenceName).executeUpdate();
        }
    }

    private void deleteEntities(Session session) {
        for (Class<?> entityType : ENTITY_TYPES) {
            session.createQuery("delete from " + entityNameOf(entityType)).executeUpdate();
        }
    }

    private String entityNameOf(Class<?> entityType) {
        return entityType.getName();
    }
}
