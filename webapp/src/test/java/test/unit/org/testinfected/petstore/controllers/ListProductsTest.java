package test.unit.org.testinfected.petstore.controllers;

import com.samskivert.mustache.Mustache;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsMapContaining;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.petstore.Page;
import org.testinfected.petstore.controllers.ListProducts;
import org.testinfected.petstore.product.AttachmentStorage;
import org.testinfected.petstore.product.Product;
import org.testinfected.petstore.product.ProductCatalog;
import test.support.org.testinfected.molecule.unit.MockRequest;
import test.support.org.testinfected.molecule.unit.MockResponse;
import test.support.org.testinfected.petstore.builders.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.any;
import static test.support.org.testinfected.molecule.unit.MockRequest.aRequest;
import static test.support.org.testinfected.molecule.unit.MockResponse.aResponse;
import static test.support.org.testinfected.petstore.builders.Builders.build;
import static test.support.org.testinfected.petstore.builders.ProductBuilder.aProduct;

@RunWith(JMock.class)
public class ListProductsTest {

    Mockery context = new JUnit4Mockery();
    ProductCatalog productCatalog = context.mock(ProductCatalog.class);
    AttachmentStorage attachmentStorage = context.mock(AttachmentStorage.class);
    Page productsPage = context.mock(Page.class);
    ListProducts listProducts = new ListProducts(productCatalog, attachmentStorage, productsPage);

    MockRequest request = aRequest();
    MockResponse response = aResponse();
    String keyword = "dogs";
    List<Product> searchResults = new ArrayList<Product>();

    @Before public void
    useDefaultPhotoForProductsMissingAPhoto() {
        context.checking(new Expectations() {{
            allowing(attachmentStorage).getLocation(with("missing.png")); will(returnValue("/photos/missing.png"));
        }});
    }

    @Before public void
    prepareRequest() {
        request.addParameter("keyword", keyword);
    }

    @SuppressWarnings("unchecked")
    @Test public void
    indicatesNoMatchWhenSearchYieldsNoResult() throws Exception {
        searchYieldsNothing();

        context.checking(new Expectations() {{
            oneOf(productsPage).render(with(response), with(hasEntry("match-found", false)));
        }});

        listProducts.handle(request, response);
    }

    @SuppressWarnings("unchecked")
    @Test public void
    rendersProductsInCatalogMatchingKeywordIfAny() throws Exception {
        searchYields(
                aProduct().withNumber("LAB-1234").named("Labrador").describedAs("Friendly dog").withPhoto("labrador.png"),
                aProduct().describedAs("Guard dog"));

        context.checking(new Expectations() {{
            allowing(attachmentStorage).getLocation(with("labrador.png")); will(returnValue("/photos/labrador.png"));
            oneOf(productsPage).render(with(response), with(allOf(hasEntry("match-found", true), hasEntry("products", searchResults))));
        }});

        listProducts.handle(request, response);
    }

    @SuppressWarnings("unchecked")
    @Test public void
    makesMatchCountAvailableToView() throws Exception {
        searchYields(aProduct(), aProduct(), aProduct());

        context.checking(new Expectations() {{
            oneOf(productsPage).render(with(response), with(hasEntry("match-count", 3)));
        }});

        listProducts.handle(request, response);
    }

    @SuppressWarnings("unchecked")
    @Test public void
    makesSearchKeywordAvailableToView() throws Exception {
        searchYields(aProduct());

        context.checking(new Expectations() {{
            oneOf(productsPage).render(with(response), with(hasEntry("keyword", keyword)));
        }});

        listProducts.handle(request, response);
    }

    @SuppressWarnings("unchecked")
    @Test public void
    makesImageResolverAvailableToView() throws Exception {
        searchYields(aProduct().withPhoto("photo.png"));

        context.checking(new Expectations() {{
            oneOf(productsPage).render(with(response), with(lambda("path")));
        }});

        listProducts.handle(request, response);
    }

    private Matcher<Map<? extends String, ?>> hasEntry(String name, Object value) {
        return Matchers.hasEntry(name, value);
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

    private IsMapContaining<String, Mustache.Lambda> lambda(String name) {
        return new IsMapContaining<String, Mustache.Lambda>(equalTo(name), any(Mustache.Lambda.class));
    }
}
