package test.com.pyxis.petstore.domain.order;

import com.pyxis.petstore.domain.order.Cart;
import com.pyxis.petstore.domain.order.CartItem;
import com.pyxis.petstore.domain.order.Cashier;
import com.pyxis.petstore.domain.product.Item;
import com.pyxis.petstore.domain.product.ItemInventory;
import com.pyxis.petstore.domain.product.ItemNumber;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.iterableWithSize;
import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;

@RunWith(JMock.class)
public class CashierTest {

    Mockery context = new JUnit4Mockery();
    ItemInventory inventory = context.mock(ItemInventory.class);

    Cart cart = new Cart();
    Cashier cashier = new Cashier(null, null, inventory, cart);

    @SuppressWarnings("unchecked")
    @Test public void
    findsItemInInventoryAndAddToCart() {
        final Item itemAlreadyInCart = anItem().withNumber("12345678").build();
        cart.add(itemAlreadyInCart);

        context.checking(new Expectations() {{
            allowing(inventory).find(with(equal(new ItemNumber("12345678")))); will(returnValue(itemAlreadyInCart));
        }});

        cashier.addToCart(new ItemNumber("12345678"));
        assertThat("order content", cashier.orderContent(), containsItems(itemWith(number("12345678"), quantity(2))));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    knowsCurrentOrderDetails() {
        cart.add(anItem().withNumber("12345678").priced("100.00").build());
        cart.add(anItem().withNumber("87654321").priced("150.00").build());

        assertThat("order total", cashier.orderTotal(), equalTo(new BigDecimal("250.00")));
        assertThat("order content", cashier.orderContent(), hasItemCount(2));
        assertThat("order content", cashier.orderContent(), containsItems(itemWith(number("12345678")), itemWith(number("87654321"))));
    }

    private Matcher<Iterable<CartItem>> hasItemCount(final int count) {
        return iterableWithSize(count);
    }

    private Matcher<? super Iterable<CartItem>> containsItems(Matcher<CartItem>... cartItemMatchers) {
        return hasItems(cartItemMatchers);
    }

    private Matcher<CartItem> itemWith(Matcher<CartItem>... itemMatchers) {
        return allOf(itemMatchers);
    }

    private Matcher<CartItem> quantity(int count) {
        return new FeatureMatcher<CartItem, Integer>(equalTo(count), "an item with quantity", "item quantity") {
            @Override protected Integer featureValueOf(CartItem actual) {
                return actual.getQuantity();
            }
        };
    }

    private Matcher<CartItem> number(String number) {
        return new FeatureMatcher<CartItem, String>(equalTo(number), "an item with number", "item number") {
            @Override protected String featureValueOf(CartItem actual) {
                return actual.getItemNumber();
            }
        };
    }
}
