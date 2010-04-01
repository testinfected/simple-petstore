package test.com.pyxis.petstore.controller;

import com.pyxis.petstore.controller.OrdersController;
import com.pyxis.petstore.domain.Cart;
import com.pyxis.petstore.domain.CreditCardType;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class OrdersControllerTest {

    Cart cart = new Cart();
    OrdersController controller = new OrdersController(cart);

    @Test public void
    checkoutRendersNewOrderForm() {
        assertThat(controller.checkout(), equalTo("orders/new"));
    }

    @Test public void
    makesCreditCardTypesAvailableToView() {
        assertThat(controller.getCreditCardTypes(), equalTo(CreditCardType.values()));
    }
}
