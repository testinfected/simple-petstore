package test.com.pyxis.petstore.controller;

import com.pyxis.petstore.controller.ItemController;
import com.pyxis.petstore.domain.Item;
import com.pyxis.petstore.domain.ItemRepository;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ItemControllerTest {

    private Mockery mockery;
    private ItemRepository itemRepository;

    @Before
    public void setUp() {
        mockery = new JUnit4Mockery();
        itemRepository = mockery.mock(ItemRepository.class);
    }

    @After
    public void assertMockeryIsSatisfied() {
        mockery.assertIsSatisfied();
    }

    @Test
    public void shouldLookUpItemsInARepositoryWhenQueryIsSubmitted() {
        final List<Item> matchingItems = Collections.emptyList();
        mockery.checking(new Expectations() {{
            oneOf(itemRepository).findItemsByKeyword("Dog");
            will(returnValue(matchingItems));
        }});
        ItemController searchController = new ItemController(itemRepository);
        ModelAndView view = searchController.doSearch("Dog");
        ModelAndViewAssert.assertModelAttributeValue(view, "matchingItems", matchingItems);
        assertThat(view.getViewName(), is("searchResults"));
    }

}
