package test.com.pyxis.petstore.domain.order;

import com.pyxis.petstore.domain.order.Cart;
import com.pyxis.petstore.domain.order.CartItem;
import com.pyxis.petstore.domain.order.Cashier;
import com.pyxis.petstore.domain.product.Item;
import com.pyxis.petstore.domain.product.ItemInventory;
import com.pyxis.petstore.domain.product.ItemNumber;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
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
        final Item item = anItem().withNumber("12345678").build();
        cart.add(item);
        cart.add(anItem().build());

        context.checking(new Expectations() {{
            allowing(inventory).find(with(equal(new ItemNumber("12345678")))); will(returnValue(item));
        }});

        cashier.addToCart(new ItemNumber("12345678"));
        assertThat("cart items", cart.getItems(), hasSize((2)));
        assertThat("cart", cashier.cartContent(), aCartContaining(itemWith(number("12345678"), quantity(2))));
    }

    private Matcher<Cart> aCartContaining(Matcher<CartItem>... cartItemMatchers) {
        return new FeatureMatcher<Cart, Iterable<CartItem>>(containsItems(cartItemMatchers), "a cart with items", "cart content") {
            @Override protected List<CartItem> featureValueOf(Cart actual) {
                return cart.getItems();
            }
        };
    }

    private Matcher<? super Iterable<CartItem>> containsItems(Matcher<CartItem>... cartItemMatchers) {
        return Matchers.hasItems(cartItemMatchers);
    }

    private Matcher<CartItem> itemWith(Matcher<CartItem>... cartItemMatchers) {
        return allOf(cartItemMatchers);
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
