package com.pyxis.petstore.persistence;

import com.pyxis.petstore.domain.Item;
import com.pyxis.petstore.domain.ItemRepository;
import com.pyxis.petstore.domain.Product;
import org.hamcrest.Matcher;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.support.com.pyxis.petstore.db.Database;

import java.util.List;

import static com.pyxis.matchers.persistence.HasFieldWithValue.hasField;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;
import static test.support.com.pyxis.petstore.db.PersistenceContext.get;

public class PersistentItemRepositoryTest {

    Database database = Database.connect(get(SessionFactory.class));
    ItemRepository itemRepository = get(ItemRepository.class);

    @Before
    public void cleanDatabase() {
        database.clean();
    }

    @After
    public void closeDatabase() {
        database.disconnect();
    }

	@Test
	public void findsItemsOfAProductGivenAProductNumber() throws Exception
	{
		Product aProduct = aProduct().withNumber("123").build();
		database.persist(aProduct);
		database.persist(anItem().of(aProduct));
		List<Item> itemsFound = itemRepository.findItemsByProductNumber("123");
		assertThat(itemsFound, contains(itemWithProduct(hasField("number", equalTo("123")))));
	}

	private Matcher<Item> itemWithProduct(Matcher<? super Product> productMatcher) {
		return hasField("product", productMatcher);
	}
	
}
