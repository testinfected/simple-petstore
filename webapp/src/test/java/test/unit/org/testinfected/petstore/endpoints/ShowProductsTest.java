package test.unit.org.testinfected.petstore.endpoints;

import com.pyxis.petstore.domain.product.ProductCatalog;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.petstore.dispatch.Dispatch;
import org.testinfected.petstore.endpoints.ShowProducts;
import test.support.org.testinfected.petstore.web.MockRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static org.hamcrest.Matchers.allOf;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;

@RunWith(JMock.class)
public class ShowProductsTest {

    Mockery context = new JUnit4Mockery();
    ProductCatalog productCatalog = context.mock(ProductCatalog.class);
    MockRequest request = new MockRequest();
    Dispatch.Response response = context.mock(Dispatch.Response.class);

    ShowProducts showProducts = new ShowProducts(productCatalog);
    String keyword = "dogs;";

    @Before public void
    prepareRequest() {
        request.addParameter("keyword", keyword);
    }

    @Test public void
    rendersNoMatchPageWithSearchKeywordWhenNoProductInCatalogMatchesKeyword() throws Exception {
        context.checking(new Expectations() {{
            oneOf(productCatalog).findByKeyword(keyword); will(returnValue(Collections.emptyList()));
            oneOf(response).render(with("no-product"), with(hasEntry("keyword", keyword)));
        }});

        showProducts.process(request, response);
    }
    
    @Test public void
    rendersProductsPageWithSearchKeywordAndMatchCountAndProductListFromCatalog() throws Exception {
        final Object matchingProducts = Arrays.asList(
                aProduct().describedAs("Friendly dog").build(),
                aProduct().describedAs("Guard dog").build());

        context.checking(new Expectations() {{
            oneOf(productCatalog).findByKeyword(keyword); will(returnValue(matchingProducts));
            oneOf(response).render(with("products"), with(allOf(
                    hasEntry("products", matchingProducts),
                    hasEntry("matchCount", 2),
                    hasEntry("keyword", keyword))));
        }});

        showProducts.process(request, response);
    }

    private Matcher<Map<? extends String, ?>> hasEntry(String name, Object matchingProducts) {
        return Matchers.hasEntry(name, matchingProducts);
    }
}
