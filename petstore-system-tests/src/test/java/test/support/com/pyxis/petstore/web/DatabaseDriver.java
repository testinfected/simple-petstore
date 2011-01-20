package test.support.com.pyxis.petstore.web;

import org.hibernate.SessionFactory;
import test.support.com.pyxis.petstore.builders.Builder;
import test.support.com.pyxis.petstore.db.Database;

import static test.support.com.pyxis.petstore.db.PersistenceContext.get;

public class DatabaseDriver {

	private Database database = new Database(get(SessionFactory.class));

	public void start() {
		database.openConnection();
		database.clean();
	}
	
	public void stop() {
		database.disconnect();
	}
	
    public <T> void contain(T... entities) throws Exception {
        database.persist(entities);
    }

    public void contain(Builder<?>... builders) throws Exception {
        database.persist(builders);
    }

    public <T> T contain(final Builder<T> builder) throws Exception {
        T entity = builder.build();
        contain(entity);
        return entity;
    }
}
