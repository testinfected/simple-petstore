package system.com.pyxis.petstore.support;

import com.pyxis.petstore.domain.Item;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

public class DatabaseSeeder {

    private final SessionFactory sessionFactory;
    private final PlatformTransactionManager transactionManager;
    private final HibernateTemplate hibernateTemplate;

    public DatabaseSeeder(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.transactionManager = new HibernateTransactionManager(sessionFactory);
        this.hibernateTemplate = new HibernateTemplate(sessionFactory);
    }

    public void perform(TransactionCallback<Object> transactionCallback) {
        TransactionTemplate transaction = new TransactionTemplate(transactionManager);
        transaction.execute(transactionCallback);
    }

	public void store(final Item item) {
        perform(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                hibernateTemplate.saveOrUpdate(item);
            }
        });
	}
}
