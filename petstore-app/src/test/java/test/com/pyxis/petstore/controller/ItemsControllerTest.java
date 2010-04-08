package test.com.pyxis.petstore.controller;

import com.pyxis.petstore.controller.ItemsController;
import com.pyxis.petstore.domain.product.Item;
import com.pyxis.petstore.domain.product.ItemInventory;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

import static com.pyxis.matchers.spring.SpringMatchers.hasAttribute;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;

@RunWith(JMock.class)
public class ItemsControllerTest {

    Mockery context = new JUnit4Mockery();
    ItemInventory itemInventory = context.mock(ItemInventory.class);
    ItemsController itemController = new ItemsController(itemInventory);

    @Test public void
    listsItemsByProductNumberAndMakeThemAvailableToView() {
    	final List<Item> anItemList = Arrays.asList(anItem().build());
    	context.checking(new Expectations(){{
    		oneOf(itemInventory).findByProductNumber("LAB-1234");
			will(returnValue(anItemList));
    	}});
        ModelAndView modelAndView = itemController.index("LAB-1234");
        assertThat(modelAndView.getViewName(), equalTo("items"));
        assertThat(modelAndView.getModelMap(), hasAttribute("itemList", anItemList));
    }
}
