package test.unit.org.testinfected.petstore.endpoints;

import com.pyxis.petstore.domain.product.AttachmentStorage;
import com.pyxis.petstore.domain.product.Product;
import com.pyxis.petstore.domain.product.ProductCatalog;
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
import org.testinfected.petstore.dispatch.Dispatch;
import org.testinfected.petstore.endpoints.ShowProducts;
import test.support.com.pyxis.petstore.builders.Builder;
import test.support.org.testinfected.petstore.web.MockRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasProperty;
import static test.support.com.pyxis.petstore.builders.Builders.build;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;

@RunWith(JMock.class)
public class ShowProductsTest {

    Mockery context = new JUnit4Mockery();
    ProductCatalog productCatalog = context.mock(ProductCatalog.class);
    AttachmentStorage attachmentStorage = context.mock(AttachmentStorage.class);
    ShowProducts showProducts = new ShowProducts(productCatalog, attachmentStorage);

    MockRequest request = new MockRequest();
    Dispatch.Response response = context.mock(Dispatch.Response.class);
    String keyword = "dogs";
    List<Product> searchResults = new ArrayList<Product>();


    @Before public void
    configureDefaultPhoto() {
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
    rendersNoMatchPageWhenSearchYieldsNoResult() throws Exception {
        searchYieldsNothing();

        context.checking(new Expectations() {{
            oneOf(response).render(with("no-results"), with(hasEntry("keyword", keyword)));
        }});

        showProducts.process(request, response);
    }

    @SuppressWarnings("unchecked")
    @Test public void
    rendersProductsPageWithProductsInCatalogThatMatchKeyword() throws Exception {
        searchYields(
                aProduct().withNumber("LAB-1234").named("Labrador").describedAs("Friendly dog").withPhoto("labrador.png"),
                aProduct().describedAs("Guard dog"));

        context.checking(new Expectations() {{
            allowing(attachmentStorage).getLocation(with("labrador.png")); will(returnValue("/photos/labrador.png"));

            oneOf(response).render(with("products"), with(
                    hasEntryMatching("products", hasItems(
                            allOf(
                                    hasProperty("number", equalTo("LAB-1234")),
                                    hasProperty("name", equalTo("Labrador")),
                                    hasProperty("description", equalTo("Friendly dog")),
                                    hasProperty("photo", equalTo("/photos/labrador.png"))),
                            hasProperty("description", equalTo("Guard dog"))
                    ))
                ));
        }});

        showProducts.process(request, response);
    }


    @Test public void
    makesMatchCountAvailableToView() throws Exception {
        searchYields(aProduct(), aProduct(), aProduct());

        context.checking(new Expectations() {{
            oneOf(response).render(with(view()), with(hasEntry("matchCount", 3)));
        }});

        showProducts.process(request, response);
    }

    @Test public void
    makesSearchKeywordAvailableToView() throws Exception {
        searchYields(aProduct());

        context.checking(new Expectations() {{
            oneOf(response).render(with(view()), with(hasEntry("keyword", keyword)));
        }});

        showProducts.process(request, response);
    }

    // Can't sort out generics on this one
    @SuppressWarnings("unchecked")
    private Matcher<Map<? extends String, ?>> hasEntryMatching(String name, Matcher value) {
        return new IsMapContaining<String, Object>(equalTo(name), value);
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
        searchResults.addAll(build(products));
        context.checking(new Expectations() {{
            allowing(productCatalog).findByKeyword(keyword); will(returnValue(searchResults));
        }});
    }
}
