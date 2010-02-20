package test.system.com.pyxis.petstore.support;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import test.integration.com.pyxis.petstore.persistence.support.ItemBuilder;
import test.integration.com.pyxis.petstore.persistence.support.Transactor;
import test.integration.com.pyxis.petstore.persistence.support.UnitOfWork;

public class DatabaseSeeder {
    private final SessionFactory sessionFactory;

    public DatabaseSeeder(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void seed(final ItemBuilder itemBuilder) throws Exception {
        final Session session = sessionFactory.openSession();
        new Transactor(session).perform(new UnitOfWork() {
            public void work() throws Exception {
                session.saveOrUpdate(itemBuilder.build());
            }
        });
        session.close();
    }
}
