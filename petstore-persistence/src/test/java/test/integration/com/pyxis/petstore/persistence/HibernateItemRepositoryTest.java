package test.integration.com.pyxis.petstore.persistence;

import com.pyxis.petstore.domain.Item;
import com.pyxis.petstore.domain.ItemRepository;
import org.hamcrest.Matcher;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.integration.com.pyxis.petstore.persistence.support.*;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static test.integration.com.pyxis.petstore.persistence.support.ItemBuilder.anItem;

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
    public void wontFindAnythingIfNoItemNameMatches() throws Exception {
        havingPersisted(anItem().withName("Dalmatian"));

        List<Item> matchingItems = itemRepository.findItemsByKeyword("Squirrel");
        assertTrue(matchingItems.isEmpty());
    }

    @Test
    public void canFindItemsWithAGivenName() throws Exception {
        havingPersisted(anItem().withName("Dalmatian"));
        havingPersisted(anItem().withName("Labrador"));

        List<Item> matches = itemRepository.findItemsByKeyword("Dalmatian");
        assertThat(matches.size(), is(equalTo(1)));
        assertThat(matches, hasItem(itemNamed("Dalmatian")));
    }

//    private <T> Matcher<? extends T> withSamePersistentFieldsAs(T entity) {
//        return SamePersistentFieldsAs.samePersistentFieldsAs(entity);
//    }

    private Matcher<Item> itemNamed(String name) {
        return HasFieldWithValue.hasField("name", equalTo(name));
    }

    private void havingPersisted(final ItemBuilder itemBuilder) throws Exception {
        transactor.perform(new UnitOfWork() {
            public void work() throws Exception {
                session.save(itemBuilder.build());
            }
        });
    }

}
