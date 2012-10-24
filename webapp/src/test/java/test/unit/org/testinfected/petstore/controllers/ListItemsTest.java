package test.unit.org.testinfected.petstore.controllers;

import com.pyxis.petstore.domain.product.Item;
import com.pyxis.petstore.domain.product.ItemInventory;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.petstore.Controller;
import org.testinfected.petstore.controllers.ListItems;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.hasEntry;
import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;

@RunWith(JMock.class)
public class ListItemsTest {

    Mockery context = new JUnit4Mockery();
    ItemInventory itemInventory = context.mock(ItemInventory.class);
    ListItems listItems = new ListItems(itemInventory);

    Controller.Request request = context.mock(Controller.Request.class);
    Controller.Response response = context.mock(Controller.Response.class);

    @Test public void
    rendersItemInInventoryMatchingProductNumber() throws Exception {
        final List<Item> items = asList(anItem().of(aProduct().withNumber("LAB-1234")).build());

        context.checking(new Expectations(){{
            allowing(itemInventory).findByProductNumber("LAB-1234"); will(returnValue(items));
        }});

        context.checking(new Expectations(){{
            allowing(request).getParameter("product"); will(returnValue("LAB-1234"));
            oneOf(response).render(with("items"), with(hasEntry("items", items)));
        }});

        listItems.process(request, response);
    }
}
