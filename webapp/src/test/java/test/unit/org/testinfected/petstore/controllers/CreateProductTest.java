package test.unit.org.testinfected.petstore.controllers;

import com.pyxis.petstore.domain.product.Product;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.petstore.Controller;
import org.testinfected.petstore.controllers.CreateProduct;
import org.testinfected.petstore.procurement.ProcurementRequestListener;

import static org.hamcrest.Matchers.samePropertyValuesAs;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;

@RunWith(JMock.class)
public class CreateProductTest {

    Mockery context = new JUnit4Mockery();
    ProcurementRequestListener requestListener = context.mock(ProcurementRequestListener.class);
    CreateProduct createProduct = new CreateProduct(requestListener);

    Controller.Request request = context.mock(Controller.Request.class);
    Controller.Response response = context.mock(Controller.Response.class);
    final int CREATED = 201;

    @Test public void
    makesProductProcurementRequestAndRespondsWithCreated() throws Exception {
        final Product product = aProduct("LAB-1234").named("Labrador").describedAs("Friendly Dog").build();
        setRequestParametersToThatOf(product);

        context.checking(new Expectations() {{
            oneOf(requestListener).addProduct(with(samePropertyValuesAs(product)));
        }});

        context.checking(new Expectations() {{
            oneOf(response).renderHead(CREATED);
        }});

        createProduct.process(request, response);

    }

    private void setRequestParametersToThatOf(final Product product) {
        context.checking(new Expectations() {{
            allowing(request).getParameter("number"); will(returnValue(product.getNumber()));
            allowing(request).getParameter("name"); will(returnValue(product.getName()));
            allowing(request).getParameter("description"); will(returnValue(product.getDescription()));
        }});
    }
}