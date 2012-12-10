package test.unit.org.testinfected.petstore.controllers;

import com.google.common.base.Function;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsMapContaining;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Action;
import org.jmock.api.Invocation;
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
import test.support.org.testinfected.petstore.builders.Builder;
import test.support.org.testinfected.molecule.unit.MockRequest;
import test.support.org.testinfected.molecule.unit.MockResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.any;
import static test.support.org.testinfected.petstore.builders.Builders.build;
import static test.support.org.testinfected.petstore.builders.ProductBuilder.aProduct;
import static test.support.org.testinfected.molecule.unit.MockRequest.aRequest;
import static test.support.org.testinfected.molecule.unit.MockResponse.aResponse;

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
            oneOf(productsPage).render(with(response), with(hasLambda("photo"))); will(call("photo", "photo.png"));
            oneOf(attachmentStorage).getLocation(with("photo.png"));
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

    private Matcher<Map<? extends String, ? extends Function>> hasLambda(String name) {
        return new IsMapContaining<String, Function>(equalTo(name), any(Function.class));
    }

    private Action call(String key, String input) {
        return new CallLambda(key, input);
    }

    public class CallLambda implements Action {
        private final String lambda;
        private String input;

        public CallLambda(String lambda, String input) {
            this.lambda = lambda;
            this.input = input;
        }

        public void describeTo(Description description) {
            description.appendText("calls " + lambda + " with ").appendText(input);
        }

        @SuppressWarnings("unchecked")
        public Object invoke(Invocation invocation) throws Throwable {
            Map<String, ?> context = ((Map<String, ?>) invocation.getParameter(1));
            ((Function) context.get(lambda)).apply(input);
            return null;
        }
    }
}
