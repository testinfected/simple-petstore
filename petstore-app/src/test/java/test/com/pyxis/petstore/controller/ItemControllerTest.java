package test.com.pyxis.petstore.controller;

import com.pyxis.petstore.controller.ItemsController;
import com.pyxis.petstore.domain.Item;
import com.pyxis.petstore.domain.ItemCatalog;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(JMock.class)
public class ItemControllerTest {

    Mockery context = new JUnit4Mockery();
    ItemCatalog itemCatalog = context.mock(ItemCatalog.class);
    ItemsController searchController = new ItemsController(itemCatalog);

    @Test
    public void listsItemMatchingKeywordAndMakesThemAvailableToView() {
        final List<Item> matchingItems = Collections.emptyList();
        context.checking(new Expectations() {{
            oneOf(itemCatalog).findItemsByKeyword("Dog");
            will(returnValue(matchingItems));
        }});

        ModelAndView view = searchController.index("Dog");
        ModelAndViewAssert.assertModelAttributeValue(view, "matchingItems", matchingItems);
        assertThat(view.getViewName(), is("searchResults"));
    }
}
