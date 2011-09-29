package test.com.pyxis.petstore.controller;

import com.pyxis.petstore.controller.ReceiptsController;
import com.pyxis.petstore.domain.order.Order;
import com.pyxis.petstore.domain.order.OrderBook;
import com.pyxis.petstore.domain.order.OrderNumber;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import static com.pyxis.matchers.spring.SpringMatchers.hasAttribute;
import static com.pyxis.petstore.Maybe.some;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static test.support.com.pyxis.petstore.builders.OrderBuilder.anOrder;

@RunWith(JMock.class)
public class ReceiptsControllerTest {

    Mockery context = new JUnit4Mockery();
    OrderBook orderBook = context.mock(OrderBook.class);
    ReceiptsController controller = new ReceiptsController(orderBook);
    Model model = new ExtendedModelMap();
    Order order = anOrder().build();

    @Test public void
    fetchesOrderByNumberAndDisplaysReceipt() {
        context.checking(new Expectations() {{
            oneOf(orderBook).find(new OrderNumber("00000100")); will(returnValue(some(order)));
        }});
        String view = controller.show("00000100", model);
        assertThat("view", view, equalTo("receipts/show"));
        assertThat("model", model, hasAttribute("order", order));
    }
}