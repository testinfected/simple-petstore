package test.unit.org.testinfected.petstore.controllers;

import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import com.vtence.molecule.http.HttpStatus;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.testinfected.petstore.controllers.CreateProduct;
import org.testinfected.petstore.procurement.ProcurementRequestHandler;
import org.testinfected.petstore.product.DuplicateProductException;

import static com.vtence.molecule.testing.ResponseAssert.assertThat;
import static test.support.org.testinfected.petstore.builders.ProductBuilder.aProduct;

public class CreateProductTest {
    @Rule public JUnitRuleMockery context = new JUnitRuleMockery();

    ProcurementRequestHandler requestHandler = context.mock(ProcurementRequestHandler.class);
    CreateProduct createProduct = new CreateProduct(requestHandler);

    Request request = new Request();
    Response response = new Response();

    @Before public void
    addProductDetailsToRequest() {
        request.addParameter("number", "LAB-1234")
               .addParameter("name", "Labrador")
               .addParameter("description", "Friendly Dog")
               .addParameter("photo", "labrador.jpg");
    }

    @Test public void
    makesProductProcurementRequestAndRespondsWithCreated() throws Exception {
        context.checking(new Expectations() {{
            oneOf(requestHandler).addProductToCatalog(with("LAB-1234"), with("Labrador"), with("Friendly Dog"), with("labrador.jpg"));
        }});

        createProduct.handle(request, response);
        assertThat(response).hasStatus(HttpStatus.CREATED).isDone();
    }

    @Test public void
    reportsResourceConflictWhenProductAlreadyExists() throws Exception {
        context.checking(new Expectations() {{
            oneOf(requestHandler).addProductToCatalog(with(any(String.class)), with(any(String.class)), with(any(String.class)), with(any(String.class)));
            will(throwException(new DuplicateProductException(aProduct().build())));
        }});

        createProduct.handle(request, response);
        assertThat(response).hasStatus(HttpStatus.CONFLICT).isDone();
    }
}