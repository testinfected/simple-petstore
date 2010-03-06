package test.com.pyxis.petstore.controller;

import static com.pyxis.matchers.spring.SpringMatchers.hasAttribute;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.springframework.ui.ModelMap;

import test.support.com.pyxis.petstore.builders.ItemBuilder;

import com.pyxis.petstore.controller.ItemsController;
import com.pyxis.petstore.domain.Item;
import com.pyxis.petstore.domain.ItemRepository;


public class ItemsControllerTest {

    Mockery context = new JUnit4Mockery();
    ItemRepository itemRepository = context.mock(ItemRepository.class);
    ItemsController itemController = new ItemsController(itemRepository);
    
    @Test
    public void listsItemsOfAProductIdentifiedByItsNumber() {
    	final List<Item> anItemList = Arrays.asList(ItemBuilder.anItem().build());
    	context.checking(new Expectations(){{
    		oneOf(itemRepository).findItemsByProductNumber("1234");
			will(returnValue(anItemList));
    	}});
    	ModelMap map = itemController.index("1234");
    	assertThat(map, hasAttribute("itemList", anItemList));
    }


}
