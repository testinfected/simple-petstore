package test.com.pyxis.petstore.controller;

import com.pyxis.petstore.controller.CartItemsController;
import com.pyxis.petstore.domain.*;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.sameInstance;
import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;

@RunWith(JMock.class)
public class CartItemsControllerTest {

    Mockery context = new JUnit4Mockery();
    Basket basket = context.mock(Basket.class);
    ItemInventory itemInventory = context.mock(ItemInventory.class);
    CartItemsController controller = new CartItemsController(basket, itemInventory);
    String ITEM_NUMBER = "11111111";

    @Test public void
    addsItemToCartAfterCheckingInventoryAndRedirectsToCart() {
        final Item item = anItem().withNumber(ITEM_NUMBER).build();
        context.checking(new Expectations() {{
            allowing(itemInventory).find(with(equal(new ItemNumber(ITEM_NUMBER)))); will(returnValue(item));
            oneOf(basket).add(with(equal(item)));
        }});
        String view = controller.create(ITEM_NUMBER);
        assertThat(view, equalTo("redirect:cartitems"));
    }

    @Test public void
    makesCartAvailableToView() {
        Basket model = controller.index();
        assertThat(model, sameInstance(basket));
    }
}
