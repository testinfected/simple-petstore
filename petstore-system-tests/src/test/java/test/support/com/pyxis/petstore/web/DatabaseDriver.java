package test.support.com.pyxis.petstore.web;

import test.support.com.pyxis.petstore.builders.EntityBuilder;
import test.support.com.pyxis.petstore.db.Database;

import static test.support.com.pyxis.petstore.web.ApplicationContext.sessionFactory;

public class DatabaseDriver {

	private Database database = new Database(sessionFactory());

	public void open() {
		database.openConnection();
		database.clean();
	}
	
	public void close() {
		database.disconnect();
	}
	
    public <T> void given(T... entities) throws Exception {
        database.persist(entities);
    }

    public void given(EntityBuilder<?>... builders) throws Exception {
        database.persist(builders);
    }

}
