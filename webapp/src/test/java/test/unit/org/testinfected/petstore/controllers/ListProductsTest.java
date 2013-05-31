package test.unit.org.testinfected.petstore.controllers;

import com.samskivert.mustache.Mustache;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.petstore.controllers.ListProducts;
import org.testinfected.petstore.product.AttachmentStorage;
import org.testinfected.petstore.product.Product;
import org.testinfected.petstore.product.ProductCatalog;
import test.support.org.testinfected.molecule.unit.MockRequest;
import test.support.org.testinfected.molecule.unit.MockResponse;
import test.support.org.testinfected.petstore.builders.Builder;
import test.support.org.testinfected.petstore.web.MockPage;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static test.support.org.testinfected.petstore.builders.Builders.build;
import static test.support.org.testinfected.petstore.builders.ProductBuilder.aProduct;

@RunWith(JMock.class)
public class ListProductsTest {

    Mockery context = new JUnit4Mockery();
    ProductCatalog productCatalog = context.mock(ProductCatalog.class);
    AttachmentStorage attachmentStorage = context.mock(AttachmentStorage.class);
    MockPage productsPage = new MockPage();
    ListProducts listProducts = new ListProducts(productCatalog, attachmentStorage, productsPage);

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
    indicatesNoMatchWhenSearchYieldsNoResult() throws Exception {
        searchYieldsNothing();
        listProducts.handle(request, response);
        productsPage.assertRenderedWith("match-found", false);
    }

    @SuppressWarnings("unchecked")
    @Test public void
    rendersProductsInCatalogMatchingKeywordIfAny() throws Exception {
        searchYields(
                aProduct().withNumber("LAB-1234").named("Labrador").describedAs("Friendly dog").withPhoto("labrador.png"),
                aProduct().describedAs("Guard dog"));

        listProducts.handle(request, response);
        productsPage.assertRenderedWith("match-found", true);
        productsPage.assertRenderedWith("products", searchResults);
    }

    @SuppressWarnings("unchecked")
    @Test public void
    makesMatchCountAvailableToView() throws Exception {
        searchYields(aProduct(), aProduct(), aProduct());
        listProducts.handle(request, response);
        productsPage.assertRenderedWith("match-count", 3);
    }

    @SuppressWarnings("unchecked")
    @Test public void
    makesSearchKeywordAvailableToView() throws Exception {
        searchYields(aProduct());
        listProducts.handle(request, response);
        productsPage.assertRenderedWith("keyword", keyword);

    }

    @SuppressWarnings("unchecked")
    @Test public void
    makesImageResolverAvailableToView() throws Exception {
        searchYields(aProduct().withPhoto("photo.png"));
        listProducts.handle(request, response);
        productsPage.assertRenderedWith(equalTo("path"), instanceOf(Mustache.Lambda.class));
    }

    @SuppressWarnings("unchecked")
    private void searchYieldsNothing() {
        searchYields();
    }

    private void searchYields(final Builder<Product>... products) {
        this.searchResults.addAll(build(products));

        context.checking(new Expectations() {{
            allowing(productCatalog).findByKeyword(keyword); will(returnValue(searchResults));
        }});
    }
}
