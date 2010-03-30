package test.com.pyxis.petstore.view;

import org.junit.Test;
import test.support.com.pyxis.petstore.views.VelocityRendering;

import static com.pyxis.matchers.dom.DomMatchers.hasUniqueSelector;
import static com.pyxis.matchers.dom.DomMatchers.withText;
import static com.threelevers.css.DocumentBuilder.dom;
import static org.hamcrest.MatcherAssert.assertThat;
import static test.support.com.pyxis.petstore.builders.CartBuilder.aCart;
import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;
import static test.support.com.pyxis.petstore.views.ModelBuilder.aModel;
import static test.support.com.pyxis.petstore.views.VelocityRendering.render;

public class NewOrderViewTest {

    String NEW_ORDER_VIEW = "order/new";
    String renderedView;

    @Test public void
    displaysOrderSummary() {
        renderedView = renderNewOrderView().using(aModel().with(aCart().containing(anItem().priced("100.00"))));
        assertThat(dom(renderedView), hasUniqueSelector("#summary .calculations .total", withText("100.00")));
    }

    private VelocityRendering renderNewOrderView() {
        return render(NEW_ORDER_VIEW);
    }
}
