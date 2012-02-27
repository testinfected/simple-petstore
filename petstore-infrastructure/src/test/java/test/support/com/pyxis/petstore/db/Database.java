package test.support.com.pyxis.petstore.db;

import org.testinfected.hamcrest.jpa.Reflection;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import test.support.com.pyxis.petstore.builders.Builder;

import javax.persistence.Id;
import java.lang.reflect.Field;

import static org.testinfected.hamcrest.jpa.SamePersistentFieldsAs.samePersistentFieldsAs;
import static org.hamcrest.MatcherAssert.assertThat;

public class Database {

    private final SessionFactory sessionFactory;
    private Session session;

    public static Database connect(SessionFactory sessionFactory) {
        Database database = new Database(sessionFactory);
        database.openConnection();
        return database;
    }

    public Database(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void openConnection() {
        session = sessionFactory.openSession();
    }

    public void clean() {
        if (session != null) new DatabaseCleaner(session).clean();
    }

    public void disconnect() {
        if (session != null) session.close();
    }

    public void persist(final Builder<?>... builders) throws Exception {
        for (final Builder<?> builder : builders) {
            persist(builder.build());
        }
    }

    public void persist(final Object... entities) throws Exception {
        for (final Object entity : entities) {
            perform(new UnitOfWork() {
                public void work(Session session) throws Exception {
                    session.save(entity);
                }
            });
        }
        makeSureSubsequentLoadOperationsHitTheDatabase();
    }

    private void makeSureSubsequentLoadOperationsHitTheDatabase() {
        session.clear();
    }

    public void perform(UnitOfWork work) throws Exception {
        Transaction transaction = session.beginTransaction();
        try {
            work.work(session);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    public void assertCanBeReloadedWithSameState(final Object original) throws Exception {
        assertCanBeReloadedWithSameState("entity", original);
    }

    public void assertCanBeReloadedWithSameState(final String entityName, final Object original) throws Exception {
        perform(new UnitOfWork() {
            public void work(Session session) throws Exception {
                Object loaded = session.get(original.getClass(), idOf(original));
                assertThat(entityName, loaded, samePersistentFieldsAs(original));
            }
        });
    }

    public static long idOf(Object entity) throws Exception {
        Class<?> type = entity.getClass();
        while (type != Object.class) {
            Field id = getId(type);
            if (id != null) return (Long) Reflection.readField(entity, id);
            type = type.getSuperclass();
        }
        throw new AssertionError("Entity has no id : " + entity);
    }

    private static Field getId(Class<?> type) {
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            if (field.getAnnotation(Id.class) != null) return field;
        }
        return null;
    }
}
