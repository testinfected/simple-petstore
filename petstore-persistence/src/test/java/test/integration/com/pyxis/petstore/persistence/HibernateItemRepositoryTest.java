package test.integration.com.pyxis.petstore.persistence;

import com.pyxis.petstore.domain.Item;
import com.pyxis.petstore.domain.ItemRepository;
import test.integration.com.pyxis.petstore.persistence.support.*;
import test.integration.com.pyxis.petstore.persistence.support.DatabaseCleaner;
import test.integration.com.pyxis.petstore.persistence.support.Transactor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class HibernateItemRepositoryTest {

    SessionFactory sessionFactory = PetStoreContext.sessionFactory();
    ItemRepository itemRepository = PetStoreContext.itemRepository();
    Session session = sessionFactory.openSession();
    Transactor transactor = new Transactor(session);

    @Before
    public void cleanDatabase() {
        new DatabaseCleaner(session).clean();
    }

    @After
    public void closeSession() {
        session.close();
    }

    @Test
    public void returnsAnEmptyListIfNoItemHasNameMatchingQuery() throws Exception {
        List<Item> matchingItems = itemRepository.findItemsByKeyword("Squirrel");
        assertTrue(matchingItems.isEmpty());
    }

    @Test
    public void returnsAListOfItemsWithNameMatchingQuery() throws Exception {
        final Item dalmatian = new Item("Dalmatian");
        dalmatian.setId(1L);
        persist(dalmatian);

        List<Item> matchingItems = itemRepository.findItemsByKeyword("Dalmatian");
        assertThat(matchingItems, hasItem(dalmatian));
    }            

    private void persist(final Item dalmatian) throws Exception {
        transactor.perform(new UnitOfWork() {
            public void work() throws Exception {
                session.save(dalmatian);
            }
        });
    }

}
