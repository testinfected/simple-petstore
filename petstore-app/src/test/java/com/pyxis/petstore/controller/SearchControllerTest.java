package com.pyxis.petstore.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.web.servlet.ModelAndView;

import com.pyxis.petstore.domain.Item;
import com.pyxis.petstore.domain.ItemRepository;

public class SearchControllerTest {

	private Mockery mockery;
	private ItemRepository itemRepository;

	@Before
	public void setUp(){
		mockery = new JUnit4Mockery();
		itemRepository = mockery.mock(ItemRepository.class);
	}
	
	@After
	public void assertMockeryIsSatisfied(){
		mockery.assertIsSatisfied();
	}
	
	@Test
	public void shouldLookUpItemsInARepositoryWhenQueryIsSubmitted()
	{		  
		final List<Item> matchingItems = Collections.emptyList();
		mockery.checking(new Expectations(){{
			oneOf(itemRepository).findItemsByQuery("Dog");
			will(returnValue(matchingItems));
		}});
		ItemController seachController = new ItemController(itemRepository);
		ModelAndView view = seachController.doSearch("Dog");
		ModelAndViewAssert.assertModelAttributeValue(view, "matchingItems", matchingItems);
		assertThat(view.getViewName(), is("searchResults"));
	}
	
}
