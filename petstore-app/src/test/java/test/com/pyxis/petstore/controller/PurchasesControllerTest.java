package test.com.pyxis.petstore.controller;

import com.pyxis.petstore.controller.PurchasesController;
import com.pyxis.petstore.domain.Cart;
import com.pyxis.petstore.domain.CheckoutAssistant;
import com.pyxis.petstore.domain.CreditCardType;
import com.pyxis.petstore.domain.Order;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.web.servlet.ModelAndView;

import static com.pyxis.matchers.spring.SpringMatchers.hasAttribute;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(JMock.class)
public class PurchasesControllerTest {

    Mockery context = new JUnit4Mockery();
    CheckoutAssistant checkoutAssistant = context.mock(CheckoutAssistant.class);
    Cart cart = new Cart();
    PurchasesController controller = new PurchasesController(cart, checkoutAssistant);

    @Test public void
    checkoutCartsAndRendersPurchaseForm() {
        final Order order = new Order();
        context.checking(new Expectations() {{
            oneOf(checkoutAssistant).checkout(cart); will(returnValue(order));
        }});
        ModelAndView modelAndView = controller.checkout();
        assertThat(modelAndView.getViewName(), equalTo("purchases/new"));
        assertThat(modelAndView.getModel(), hasAttribute("order", order));
    }

    @Test public void
    makesCreditCardTypesAvailableToView() {
        assertThat(controller.getCreditCardTypes(), equalTo(CreditCardType.values()));
    }
}
