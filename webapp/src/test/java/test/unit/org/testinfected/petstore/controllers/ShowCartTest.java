package test.unit.org.testinfected.petstore.controllers;

import com.pyxis.petstore.domain.order.Cart;
import com.pyxis.petstore.domain.order.SalesAssistant;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.petstore.Controller;
import org.testinfected.petstore.controllers.ShowCart;

import static test.support.com.pyxis.petstore.builders.CartBuilder.aCart;
import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;

@RunWith(JMock.class)
public class ShowCartTest {

    Mockery context = new JUnit4Mockery();
    SalesAssistant salesAssistant = context.mock(SalesAssistant.class);
    ShowCart showCart = new ShowCart(salesAssistant);

    Controller.Request request = context.mock(Controller.Request.class);
    Controller.Response response = context.mock(Controller.Response.class);

    @Test public void
    makesCartContentAvailableToView() throws Exception {
        final Cart cart = aCart().containing(anItem()).build();

        context.checking(new Expectations() {{
            allowing(salesAssistant).cartContent(); will(returnValue(cart));
            oneOf(response).render(with("cart"), with(same(cart)));
        }});

        showCart.process(request, response);
    }
}
