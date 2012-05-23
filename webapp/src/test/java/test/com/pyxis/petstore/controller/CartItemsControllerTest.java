package test.com.pyxis.petstore.controller;

import com.pyxis.petstore.controller.CartItemsController;
import com.pyxis.petstore.domain.order.Basket;
import com.pyxis.petstore.domain.product.Item;
import com.pyxis.petstore.domain.product.ItemInventory;
import com.pyxis.petstore.domain.product.ItemNumber;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.testinfected.hamcrest.spring.SpringMatchers.isRedirectedTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;
import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;

@RunWith(JMock.class)
public class CartItemsControllerTest {

    Mockery context = new JUnit4Mockery();
    Basket basket = context.mock(Basket.class);
    ItemInventory itemInventory = context.mock(ItemInventory.class);
    CartItemsController controller = new CartItemsController(basket, itemInventory);

    @Test public void
    addsItemToCartAfterCheckingInventoryAndRedirectsToCart() {
        final String itemNumber = "12345678";
        final Item item = anItem().withNumber(itemNumber).build();
        context.checking(new Expectations() {{
            allowing(itemInventory).find(with(equal(new ItemNumber(itemNumber)))); will(returnValue(item));
            oneOf(basket).add(with(equal(item)));
        }});

        String view = controller.create(itemNumber);
        assertThat("view", view, isRedirectedTo("/cart"));
    }

    @Test public void
    makesCartAvailableToView() {
        Basket model = controller.index();
        assertThat("model", model, sameInstance(basket));
    }
}
