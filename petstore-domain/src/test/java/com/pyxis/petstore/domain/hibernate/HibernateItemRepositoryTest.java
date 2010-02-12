package com.pyxis.petstore.domain.hibernate;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.pyxis.petstore.domain.Item;
import com.pyxis.petstore.domain.ItemRepository;

public class HibernateItemRepositoryTest {

	private ItemRepository itemRepository;
	private InMemoryDatabase database;

	@Before
	public void setUp() throws Exception {
		database = new InMemoryDatabase();
		itemRepository = new HibernateItemRepository(database.hibernateTemplate());
	}

	@Test
	public void returnsAnEmptyListIfNoItemHasNameMatchingQuery() {
		List<Item> matchingItems = itemRepository.findItemsByQuery("Squirrel");
		assertTrue(matchingItems.isEmpty());
	}

	@Test
	public void returnsAListOfItemsWithNameMatchingQuery() {
		Item dalmatian = new Item("Dalmatian");
		dalmatian.setId(1L);
		database.save(dalmatian);
		List<Item> matchingItems = itemRepository.findItemsByQuery("Dalmatian");
		assertThat(matchingItems, hasItem(dalmatian));		
	}

}
