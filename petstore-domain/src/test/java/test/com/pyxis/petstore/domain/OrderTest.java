package test.com.pyxis.petstore.domain;

import com.pyxis.petstore.domain.*;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.pyxis.petstore.domain.CreditCard.visa;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static test.support.com.pyxis.petstore.builders.CartBuilder.aCart;
import static test.support.com.pyxis.petstore.builders.CartBuilder.anEmptyCart;
import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;
import static test.support.com.pyxis.petstore.builders.OrderBuilder.anOrder;

public class OrderTest {

    Order order = anOrder().build();

    @Test public void
    consistsOfNoItemByDefault() {
        assertThat(order.getLineItems(), Matchers.<LineItem>empty());
    }

    @Test public void
    consistsOfNoItemIsCartIsEmpty() {
        order.addItemsFromCart(anEmptyCart().build());
        assertThat(order.getLineItems(), Matchers.<LineItem>empty());
    }

    @Test public void
    addsLineItemsFromCart() {
        Cart cart = aCartWithSomeItemsAddedMultipleTimes();
        order.addItemsFromCart(cart);

        assertThat(order.getLineItems(), containsLineItems(matchingItemsOf(cart)));
        assertThat(cart.getTotalQuantity(), equalTo(cart.getTotalQuantity()));
    }

    @Test public void
    isInsensitiveToChangeInItemPrice() {
        BigDecimal originalPrice = new BigDecimal("75.97");
        Item anItemWhichPriceWillChange = anItem().priced(originalPrice).build();
        Cart cart = aCart().containing(anItemWhichPriceWillChange).build();
        order.addItemsFromCart(cart);

        BigDecimal updatedPrice = new BigDecimal("84.99");
        anItemWhichPriceWillChange.setPrice(updatedPrice);
        assertThat(order.getTotalPrice(), equalTo(originalPrice));
    }

    @Test(expected = UnsupportedOperationException.class) public void
    cannotBeModified() {
        order.getLineItems().clear();
    }

    @Test public void
    calculatesGrandTotal() {
        Cart cart = aCartWithManyItems();
        order.addItemsFromCart(cart);
        assertThat(order.getTotalPrice(), equalTo(cart.getGrandTotal()));
    }
    
    @Test public void
    indicatesPaidWhenPaymentWasReceived() {
        Order order = anOrder().build();
        assertFalse(order.isPaid());
        order.paidWith(visa("9999 9999 9999", "12/12"));
        assertTrue(order.isPaid());
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

    private Matcher<Iterable<LineItem>> containsLineItems(List<Matcher<? super LineItem>> lineItemMatchers) {
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

    private Matcher<LineItem> with(Matcher<LineItem>... lineItemMatchers) {
        return allOf(lineItemMatchers);
    }

    private Matcher<LineItem> quantity(int count) {
        return hasProperty("quantity", equalTo(count));
    }

    private Matcher<LineItem> number(String number) {
        return hasProperty("itemNumber", equalTo(number));
    }

    private Matcher<LineItem> totalPrice(BigDecimal price) {
        return hasProperty("totalPrice", equalTo(price));
    }
}