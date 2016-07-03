package test.unit.org.testinfected.petstore.controllers;

import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.testinfected.petstore.controllers.ListProducts;
import org.testinfected.petstore.product.AttachmentStorage;
import org.testinfected.petstore.product.Product;
import org.testinfected.petstore.product.ProductCatalog;
import org.testinfected.petstore.views.Products;
import test.support.org.testinfected.petstore.builders.Builder;
import test.support.org.testinfected.petstore.web.MockView;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static test.support.org.testinfected.petstore.builders.Builders.build;
import static test.support.org.testinfected.petstore.builders.ProductBuilder.aProduct;

public class ListProductsTest {
    @Rule public JUnitRuleMockery context = new JUnitRuleMockery();

    ProductCatalog productCatalog = context.mock(ProductCatalog.class);
    AttachmentStorage photoLibrary = context.mock(AttachmentStorage.class);
    MockView<Products> view = new MockView<>();
    ListProducts listProducts = new ListProducts(productCatalog, photoLibrary, view);

    Request request = new Request();
    Response response = new Response();

    String keyword = "dogs";
    List<Product> searchResults = new ArrayList<>();

    @Before public void
    addSearchKeywordToRequest() {
        request.addParameter("keyword", keyword);
    }

    @After public void
    assertPageRendered() {
        view.assertRenderedTo(response);
    }

    @Test public void
    rendersProductsInCatalogMatchingKeyword() throws Exception {
        searchYields(
                aProduct().withNumber("LAB-1234").named("Labrador").describedAs("Friendly dog").withPhoto("labrador.png"),
                aProduct().describedAs("Guard dog"));

        listProducts.handle(request, response);
        view.assertRenderedWith(productsFound(searchResults));
        view.assertRenderedWith(searchKeyword(keyword));
        view.assertRenderedWith(photosIn(photoLibrary));
    }

    private Matcher<Object> photosIn(AttachmentStorage photos) {
        return hasProperty("photos", equalTo(photos));
    }

    private Matcher<Object> searchKeyword(String keyword) {
        return hasProperty("keyword", equalTo(keyword));
    }

    private Matcher<Object> productsFound(List<Product> results) {
        return hasProperty("each", equalTo(results));
    }

    @SafeVarargs
    private final void searchYields(final Builder<Product>... products) {
        this.searchResults.addAll(build(products));

        context.checking(new Expectations() {{
            allowing(productCatalog).findByKeyword(keyword); will(returnValue(searchResults));
        }});
    }
}