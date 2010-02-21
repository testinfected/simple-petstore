package test.integration.com.pyxis.petstore.persistence.support;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class Database {

    private final SessionFactory sessionFactory;
    private Session session;

    public Database(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Database connect() {
        openConnection();
        return this;
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

    public void persist(final EntityBuilder<?>... builders) throws Exception {
        for (final EntityBuilder builder : builders) {
            perform(new UnitOfWork() {
                public void work() throws Exception {
                    session.save(builder.build());
                }
            });
        }
    }

    public void perform(UnitOfWork work) throws Exception {
        new Transactor(session).perform(work);
    }
}
