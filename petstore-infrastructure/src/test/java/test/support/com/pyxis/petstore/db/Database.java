package test.support.com.pyxis.petstore.db;

import com.pyxis.helpers.Reflection;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import test.support.com.pyxis.petstore.builders.Builder;

import javax.persistence.Id;
import java.lang.reflect.Field;

import static com.pyxis.matchers.persistence.SamePersistentFieldsAs.samePersistentFieldsAs;
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
        new DatabaseCleaner(session).clean();
    }

    public void disconnect() {
        session.close();
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

    public void assertCanBeReloadedWithSameState(final Object entity) throws Exception {
        perform(new UnitOfWork() {
            public void work(Session session) throws Exception {
                Object persisted = session.get(entity.getClass(), idOf(entity));
                assertThat(persisted, samePersistentFieldsAs(entity));
            }
        });
    }

    public static long idOf(Object entity) throws Exception {
        Field[] fields =  entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getAnnotation(Id.class) != null) return (Long) Reflection.readField(entity, field);
        }
        throw new AssertionError("Entity has no id : " + entity );
    }
}
