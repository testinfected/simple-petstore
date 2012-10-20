package test.unit.org.testinfected.petstore.controllers;

import com.google.common.base.Function;
import com.pyxis.petstore.domain.product.AttachmentStorage;
import com.pyxis.petstore.domain.product.Product;
import com.pyxis.petstore.domain.product.ProductCatalog;
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
import org.testinfected.petstore.controllers.ListProducts;
import org.testinfected.petstore.Controller;
import test.support.com.pyxis.petstore.builders.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.any;
import static test.support.com.pyxis.petstore.builders.Builders.build;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;

@RunWith(JMock.class)
public class ListProductsTest {

    Mockery context = new JUnit4Mockery();
    ProductCatalog productCatalog = context.mock(ProductCatalog.class);
    AttachmentStorage attachmentStorage = context.mock(AttachmentStorage.class);
    ListProducts listProducts = new ListProducts(productCatalog, attachmentStorage);

    Controller.Request request = context.mock(Controller.Request.class);
    Controller.Response response = context.mock(Controller.Response.class);
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
        context.checking(new Expectations() {{
            allowing(request).getParameter("keyword");
            will(returnValue(keyword));
        }});
    }

    @SuppressWarnings("unchecked")
    @Test public void
    rendersNoMatchWhenSearchYieldsNoResult() throws Exception {
        searchYieldsNothing();

        context.checking(new Expectations() {{
            oneOf(response).render(with("products"), with(hasEntry("match-found", false)));
        }});

        listProducts.process(request, response);
    }

    @SuppressWarnings("unchecked")
    @Test public void
    rendersProductsInCatalogMatchingKeywordIfAny() throws Exception {
        searchYields(
                aProduct().withNumber("LAB-1234").named("Labrador").describedAs("Friendly dog").withPhoto("labrador.png"),
                aProduct().describedAs("Guard dog"));

        context.checking(new Expectations() {{
            allowing(attachmentStorage).getLocation(with("labrador.png")); will(returnValue("/photos/labrador.png"));
            oneOf(response).render(with("products"), with(allOf(hasEntry("match-found", true), hasEntry("products", searchResults))));
        }});

        listProducts.process(request, response);
    }

    @SuppressWarnings("unchecked")
    @Test public void
    makesMatchCountAvailableToView() throws Exception {
        searchYields(aProduct(), aProduct(), aProduct());

        context.checking(new Expectations() {{
            oneOf(response).render(with(view()), with(hasEntry("matchCount", 3)));
        }});

        listProducts.process(request, response);
    }

    @SuppressWarnings("unchecked")
    @Test public void
    makesSearchKeywordAvailableToView() throws Exception {
        searchYields(aProduct());

        context.checking(new Expectations() {{
            oneOf(response).render(with(view()), with(hasEntry("keyword", keyword)));
        }});

        listProducts.process(request, response);
    }

    @SuppressWarnings("unchecked")
    @Test public void
    makesImageResolverAvailableToView() throws Exception {
        searchYields(aProduct().withPhoto("photo.png"));

        context.checking(new Expectations() {{
            oneOf(response).render(with(view()), with(hasLambda("photo"))); will(call("photo", "photo.png"));
            oneOf(attachmentStorage).getLocation(with("photo.png"));
        }});

        listProducts.process(request, response);
    }

    private Matcher<Map<? extends String, ?>> hasEntry(String name, Object value) {
        return Matchers.hasEntry(name, value);
    }

    private Matcher<String> view() {
        return any(String.class);
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
