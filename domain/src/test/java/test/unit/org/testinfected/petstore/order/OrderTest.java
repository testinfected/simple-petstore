package test.unit.org.testinfected.petstore.order;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.testinfected.petstore.order.Cart;
import org.testinfected.petstore.order.CartItem;
import org.testinfected.petstore.order.LineItem;
import org.testinfected.petstore.order.Order;
import org.testinfected.petstore.product.Item;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static test.support.org.testinfected.petstore.builders.CartBuilder.aCart;
import static test.support.org.testinfected.petstore.builders.CartBuilder.anEmptyCart;
import static test.support.org.testinfected.petstore.builders.CreditCardBuilder.aVisa;
import static test.support.org.testinfected.petstore.builders.ItemBuilder.anItem;
import static test.support.org.testinfected.petstore.builders.OrderBuilder.anOrder;

public class OrderTest {

    Order order = anOrder().build();

    @Test public void
    consistsOfNoItemByDefault() {
        assertThat("line items", order.getLineItems(), Matchers.empty());
    }

    @Test public void
    consistsOfNoItemIfCartIsEmpty() {
        order.addItemsFrom(anEmptyCart().build());
        assertThat("line items", order.getLineItems(), Matchers.empty());
    }

    @Test public void
    addsLineItemsFromCart() {
        Cart cart = aCartWithSomeItemsAddedMultipleTimes();
        order.addItemsFrom(cart);

        assertThat("line items", order.getLineItems(), containsLineItems(matchingItemsOf(cart)));
        assertThat("total quantity", order.getTotalQuantity(), equalTo(cart.getTotalQuantity()));
    }

    @Test public void
    isInsensitiveToChangeInItemPrice() {
        BigDecimal originalPrice = new BigDecimal("75.97");
        Item anItemWhosePriceWillChange = anItem().priced(originalPrice).build();
        Cart cart = aCart().containing(anItemWhosePriceWillChange).build();
        order.addItemsFrom(cart);

        BigDecimal updatedPrice = new BigDecimal("84.99");
        anItemWhosePriceWillChange.setPrice(updatedPrice);
        assertThat("total price", order.getTotalPrice(), equalTo(originalPrice));
    }

    @Test(expected = UnsupportedOperationException.class) public void
    cannotBeModified() {
        order.getLineItems().clear();
    }

    @Test public void
    calculatesGrandTotal() {
        Cart cart = aCartWithManyItems();
        order.addItemsFrom(cart);
        assertThat("total price", order.getTotalPrice(), equalTo(cart.getGrandTotal()));
    }
    
    @Test public void
    indicatesPaidWhenPaymentWasReceived() {
        Order order = anOrder().build();
        assertFalse("paid", order.isPaid());
        order.pay(aVisa().build());
        assertTrue("not paid", order.isPaid());
    }

    private Cart aCartWithSomeItemsAddedMultipleTimes() {
        Cart cart = aCart().build();
        String[] itemNumbers = { "11111111", "11111111", "22222222" };
        for (String number : itemNumbers) {
            cart.add(anItem().withNumber(number).build());
        }
        return cart;
    }

    private Cart aCartWithManyItems() {
        Cart cart = aCart().build();
        for (int i = 0; i < 10; i++) { cart.add(anItem().build()); }
        return cart;
    }

    private Matcher<Iterable<? extends LineItem>> containsLineItems(List<Matcher<? super LineItem>> lineItemMatchers) {
        return Matchers.contains(lineItemMatchers);
    }

    private List<Matcher<? super LineItem>> matchingItemsOf(Cart cart) {
        List<Matcher<? super LineItem>> all = new ArrayList<Matcher<? super LineItem>>();
        for (CartItem cartItem : cart.getItems()) {
            all.add(matchingCartItem(cartItem));
        }
        return all;
    }

    private Matcher<LineItem> matchingCartItem(CartItem cartItem) {
        return with(number(cartItem.getItemNumber()), quantity(cartItem.getQuantity()), totalPrice(cartItem.getTotalPrice()));
    }

    @SafeVarargs
    private final Matcher<LineItem> with(Matcher<LineItem>... lineItemMatchers) {
        return allOf(lineItemMatchers);
    }

    private Matcher<LineItem> quantity(int count) {
        return new FeatureMatcher<LineItem, Integer>(equalTo(count), " a line item with quantity", "quantity") {
            protected Integer featureValueOf(LineItem actual) {
                return actual.getQuantity();
            }
        };
    }

    private Matcher<LineItem> number(String number) {
        return new FeatureMatcher<LineItem, String>(equalTo(number), "a line item with number", "item number") {
            protected String featureValueOf(LineItem actual) {
                return actual.getItemNumber();
            }
        };
    }

    private Matcher<LineItem> totalPrice(BigDecimal price) {
        return new FeatureMatcher<LineItem, BigDecimal>(equalTo(price), "a line item with total", "total") {
            protected BigDecimal featureValueOf(LineItem actual) {
                return actual.getTotalPrice();
            }
        };
    }
}