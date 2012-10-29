package test.unit.org.testinfected.petstore.controllers;

import com.pyxis.petstore.domain.product.Item;
import com.pyxis.petstore.domain.product.Product;
import com.pyxis.petstore.domain.product.ProductCatalog;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.petstore.Controller;
import org.testinfected.petstore.controllers.CreateItem;
import org.testinfected.petstore.procurement.ProcurementRequestListener;

import static org.hamcrest.Matchers.samePropertyValuesAs;
import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;

@RunWith(JMock.class)
public class CreateItemTest {

    Mockery context = new JUnit4Mockery();
    ProcurementRequestListener requestListener = context.mock(ProcurementRequestListener.class);
    ProductCatalog productCatalog = context.mock(ProductCatalog.class);
    CreateItem createItem = new CreateItem(productCatalog, requestListener);

    Controller.Request request = context.mock(Controller.Request.class);
    Controller.Response response = context.mock(Controller.Response.class);
    final int CREATED = 201;

    @Test public void
    makesItemProcurementRequestAndRespondsWithCreated() throws Exception {
        final Product product = aProduct().withNumber("LAB-1234").build();
        final Item item = anItem().of(product).withNumber("12345678").
                describedAs("Chocolate Male").priced("599.00").build();
        setRequestParametersToThatOf(item);

        context.checking(new Expectations() {{
            allowing(productCatalog).findByNumber("LAB-1234"); will(returnValue(product));
            oneOf(requestListener).addItem(with(samePropertyValuesAs(item)));
        }});

        context.checking(new Expectations() {{
            oneOf(response).renderHead(CREATED);
        }});

        createItem.process(request, response);
    }

    private void setRequestParametersToThatOf(final Item item) {
        context.checking(new Expectations() {{
            allowing(request).getParameter("product"); will(returnValue(item.getProductNumber()));
            allowing(request).getParameter("number"); will(returnValue(item.getNumber()));
            allowing(request).getParameter("description"); will(returnValue(item.getDescription()));
            allowing(request).getParameter("price"); will(returnValue(item.getPrice().toPlainString()));
        }});
    }
}
