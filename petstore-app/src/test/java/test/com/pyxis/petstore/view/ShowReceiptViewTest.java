package test.com.pyxis.petstore.view;

import com.pyxis.petstore.domain.product.Item;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import test.support.com.pyxis.petstore.views.ModelBuilder;
import test.support.com.pyxis.petstore.views.VelocityRendering;

import static com.pyxis.matchers.dom.DomMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static test.support.com.pyxis.petstore.builders.AddressBuilder.anAddress;
import static test.support.com.pyxis.petstore.builders.CartBuilder.aCart;
import static test.support.com.pyxis.petstore.builders.CreditCardBuilder.aVisa;
import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;
import static test.support.com.pyxis.petstore.builders.OrderBuilder.anOrder;
import static test.support.com.pyxis.petstore.views.ModelBuilder.aModel;
import static test.support.com.pyxis.petstore.views.PathFor.homePath;
import static test.support.com.pyxis.petstore.views.VelocityRendering.render;

public class ShowReceiptViewTest {

    String SHOW_RECEIPT_VIEW_NAME = "receipts/show";
    Element showReceiptView;
    ModelBuilder model;
    String orderTotal = "458.97";

    @Before public void
    renderView() {
        Item anItemOrderedSeveralTimes = anItem().
                withNumber("12345678").
                describedAs("Green Adult").
                priced("100.00").build();
        Item anItemOrderedOnce = anItem().priced("258.97").build();
        model = aModel().with(
                    anOrder().
                        withNumber("00000100").
                        from(aCart().containing(
                                anItemOrderedSeveralTimes,
                                anItemOrderedSeveralTimes,
                                anItemOrderedOnce)).
                        paidWith(aVisa().
                            withNumber("9999 9999 9999").
                            withExpiryDate("12/12").
                            billedTo(anAddress().
                                withFirstName("John").
                                withLastName("Doe").
                                withEmail("jdoe@gmail.com")
                            )
                        )
                    );

        showReceiptView = renderShowReceiptView().using(model).asDom();
    }

    @Test public void
    displaysOrderSummary() {
        assertThat(showReceiptView, hasOrderNumber("00000100"));
        assertThat(showReceiptView, hasOrderTotal(orderTotal));
    }

    @Test public void
    setsUpOrderDetailsColumnHeadings() {
        assertThat(showReceiptView,
                hasSelector("#order-details th",
                        inOrder(withText("Quantity"),
                                withText("Item"),
                                withText("Price"),
                                withText("Total"))));
    }

    @Test public void
    displaysOrderLineItemsInColumns() {
        assertThat(showReceiptView, hasSelector("#order-details tr#line-item-12345678 td",
                inOrder(withText("2"),
                        withText(containsString("Green Adult")),
                        withText("100.00"),
                        withText("200.00"))));
    }

    @Test public void
    displaysOneOrderLineItemPerLine() {
        assertThat(showReceiptView, hasSelector("#order-details tr.line-item", withSize(2)));
    }

    private Matcher<Element> hasOrderTotal(final String orderTotal) {
        return hasUniqueSelector("#order-details",
                hasUniqueSelector(".calculations .total", withText(orderTotal))
        );
    }

    private Matcher<Element> hasOrderNumber(final String orderNumber) {
        return hasUniqueSelector("#order .number", withText(endsWith(orderNumber)));
    }

    @Test public void
    displaysPaymentDetails() {
        assertThat(showReceiptView, hasSelector("#payment-details li span:nth-child(2)", inOrder(
                withText("Visa"),
                withText("9999 9999 9999"),
                withText("12/12"))));
    }

    @Test public void
    displaysBillingInformation() {
        assertThat(showReceiptView, hasSelector("#billing-address li span:nth-child(2)", inOrder(
                withText("John"),
                withText("Doe"),
                withText("jdoe@gmail.com"))));
    }

    @Test public void
    returnsToHomePageToContinueShopping() {
        assertThat(showReceiptView, hasUniqueSelector("a#continue-shopping", withAttribute("href", homePath())));
    }

    private VelocityRendering renderShowReceiptView() {
        return render(SHOW_RECEIPT_VIEW_NAME);
    }
}