package test.integration.org.testinfected.petstore.controller;

import com.pyxis.petstore.domain.product.Product;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.petstore.Server;
import org.testinfected.petstore.controller.CreateProduct;
import org.testinfected.petstore.procurement.ProcurementRequestListener;
import test.support.org.testinfected.petstore.web.HttpRequest;
import test.support.org.testinfected.petstore.web.HttpResponse;

import java.io.IOException;

import static org.hamcrest.Matchers.samePropertyValuesAs;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;
import static test.support.org.testinfected.petstore.web.HttpRequest.aRequest;

@RunWith(JMock.class)
public class CreateProductTest {

    Mockery context = new JUnit4Mockery();
    ProcurementRequestListener requestListener = context.mock(ProcurementRequestListener.class);
    CreateProduct createProduct = new CreateProduct(requestListener);

    Server server = new Server(9999);
    HttpRequest request = aRequest().to(server);
    final int CREATED = 201;

    @Before public void
    startServer() throws IOException {
        server.run(createProduct);
    }

    @After public void
    stopServer() throws Exception {
        server.shutdown();
    }

    @Test public void
    makesProductProcurementRequestAndRespondsWithCreated() throws Exception {
        final Product product = aProduct("LAB-1234").named("Labrador").describedAs("Friendly Dog").build();
        context.checking(new Expectations() {{
            oneOf(requestListener).addProduct(with(samePropertyValuesAs(product)));
        }});

        HttpResponse response = request.
                withParameter("number", product.getNumber()).
                withParameter("name", product.getName()).
                withParameter("description", product.getDescription()).
                send();

        context.assertIsSatisfied();

        response.assertHasStatusCode(CREATED);
        response.assertHasStatusMessage("Created");
    }
}