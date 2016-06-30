package test.unit.org.testinfected.petstore.order;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.testinfected.petstore.order.Cart;
import org.testinfected.petstore.order.CartItem;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static test.support.org.testinfected.petstore.builders.ItemBuilder.anItem;
import static test.support.org.testinfected.petstore.matchers.SerializedForm.serializedForm;

public class CartTest {
    Cart cart = new Cart();

    @Test public void
    isEmptyByDefault() {
        assertThat("contains item(s)", cart.empty());
        assertThat("grand total", cart.getGrandTotal(), equalTo(BigDecimal.ZERO));
        assertThat("total quantity", cart.getTotalQuantity(), equalTo(0));
    }

    @Test public void
    containsCartItemsInBuyingOrder() {
        String[] itemNumbers = { "11111111", "22222222", "33333333" };
        for (String itemNumber : itemNumbers) {
            cart.add(anItem().withNumber(itemNumber).build());
        }
        assertThat("empty cart", !cart.empty());
        assertThat("cart", cart, aCartContaining(
                itemWith(number("11111111")),
                itemWith(number("22222222")),
                itemWith(number("33333333"))));
        assertThat("total quantity", cart.getTotalQuantity(), equalTo(3));
    }

    @Test(expected = UnsupportedOperationException.class) public void
    listOfItemsCannotBeModified() {
        cart.getItems().clear();
    }

    @Test public void
    calculatesGrandTotal() {
        String[] prices = { "50", "75.50", "12.75" };
        BigDecimal expectedTotal = new BigDecimal("138.25");

        for (String price : prices) {
            cart.add(anItem().priced(price).build());
        }
        assertThat("grand total", cart.getGrandTotal(), equalTo(expectedTotal));
    }

    @Test public void
    groupsItemsByNumber() {
        String[] itemNumbers = { "11111111", "11111111", "22222222" };

        for (String number : itemNumbers) {
            cart.add(anItem().withNumber(number).build());
        }
        assertThat("cart", cart, aCartContaining(
                itemWith(number("11111111"), quantity(2)),
                itemWith(number("22222222"), quantity(1))));
        assertThat("total quantity", cart.getTotalQuantity(), equalTo(3));
    }
    
    @Test public void
    canBeCleared() {
        havingAddedItemsToCart();

        cart.clear();
        assertThat("contains item(s)", cart.empty());
    }

    @Test public void
    isSerializable() {
        cart.add(anItem().withNumber("11111111").build());
        assertThat("cart", cart, serializedForm(aCartContaining(itemWith(number("11111111")))));
    }

    private void havingAddedItemsToCart() {
        cart.add(anItem().build());
        cart.add(anItem().build());
        cart.add(anItem().build());
    }

    @SafeVarargs
    private final Matcher<Cart> aCartContaining(Matcher<? super CartItem>... cartItemMatchers) {
        return new FeatureMatcher<Cart, Iterable<CartItem>>(containsItems(asList(cartItemMatchers)), "a cart with items", "cart content") {
            protected List<CartItem> featureValueOf(Cart actual) {
                return cart.getItems();
            }
        };
    }

    private Matcher<? super Iterable<CartItem>> containsItems(List<Matcher<? super CartItem>> cartItemMatchers) {
        return contains(cartItemMatchers);
    }

    @SafeVarargs
    private final Matcher<CartItem> itemWith(Matcher<CartItem>... cartItemMatchers) {
        return allOf(cartItemMatchers);
    }

    private Matcher<CartItem> quantity(int count) {
        return new FeatureMatcher<CartItem, Integer>(equalTo(count), "an item with quantity", "item quantity") {
            protected Integer featureValueOf(CartItem actual) {
                return actual.getQuantity();
            }
        };
    }

    private Matcher<CartItem> number(String number) {
        return new FeatureMatcher<CartItem, String>(equalTo(number), "an item with number", "item number") {
            protected String featureValueOf(CartItem actual) {
                return actual.getItemNumber();
            }
        };
    }
}