package test.unit.org.testinfected.petstore.controllers;

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
import org.testinfected.petstore.views.ProductsFound;
import test.support.org.testinfected.molecule.unit.MockRequest;
import test.support.org.testinfected.molecule.unit.MockResponse;
import test.support.org.testinfected.petstore.builders.Builder;
import test.support.org.testinfected.petstore.web.MockPage;

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
    MockPage productsPage = new MockPage();
    ListProducts listProducts = new ListProducts(productCatalog, photoLibrary, productsPage);

    MockRequest request = new MockRequest();
    MockResponse response = new MockResponse();

    String keyword = "dogs";
    List<Product> searchResults = new ArrayList<Product>();

    @Before public void
    addSearchKeywordToRequest() {
        request.addParameter("keyword", keyword);
    }

    @After public void
    assertPageRendered() {
        productsPage.assertRenderedTo(response);
    }

    @SuppressWarnings("unchecked")
    @Test public void
    rendersProductsInCatalogMatchingKeyword() throws Exception {
        searchYields(
                aProduct().withNumber("LAB-1234").named("Labrador").describedAs("Friendly dog").withPhoto("labrador.png"),
                aProduct().describedAs("Guard dog"));

        listProducts.handle(request, response);
        productsPage.assertRenderingContext(productsFound(searchResults));
        productsPage.assertRenderingContext(searchKeyword(keyword));
        productsPage.assertRenderingContext(photosIn(photoLibrary));
    }

    private Matcher<Object> photosIn(AttachmentStorage photos) {
        return hasProperty("photos", equalTo(photos));
    }

    private Matcher<ProductsFound> searchKeyword(String keyword) {
        return hasProperty("keyword", equalTo(keyword));
    }

    private Matcher<ProductsFound> productsFound(List<Product> results) {
        return hasProperty("each", equalTo(results));
    }

    private void searchYields(final Builder<Product>... products) {
        this.searchResults.addAll(build(products));

        context.checking(new Expectations() {{
            allowing(productCatalog).findByKeyword(keyword); will(returnValue(searchResults));
        }});
    }
}
