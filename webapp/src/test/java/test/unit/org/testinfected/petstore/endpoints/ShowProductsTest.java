package test.unit.org.testinfected.petstore.endpoints;

import com.pyxis.petstore.domain.product.ProductCatalog;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.petstore.dispatch.Dispatch;
import org.testinfected.petstore.endpoints.ShowProducts;
import test.support.org.testinfected.petstore.web.MockRequest;

import java.util.Arrays;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;

@RunWith(JMock.class)
public class ShowProductsTest {

    Mockery context = new JUnit4Mockery();
    ProductCatalog productCatalog = context.mock(ProductCatalog.class);
    MockRequest request = new MockRequest();
    Dispatch.Response response = context.mock(Dispatch.Response.class);

    ShowProducts showProducts = new ShowProducts(productCatalog);

    @Test public void
    searchesForProductsInCatalogUsingSpecifiedKeywordThenRendersProductsPageWithSearchKeywordAndProductList() throws Exception {
        final String keyword = "dogs;";
        request.addParameter("keyword", keyword);
        final Object matchingProducts = Arrays.asList(aProduct().describedAs("Friendly dog").build());

        context.checking(new Expectations() {{
            oneOf(productCatalog).findByKeyword(keyword); will(returnValue(matchingProducts));
            oneOf(response).render(with("products"), with(allOf(
                    hasEntry("products", matchingProducts),
                    hasEntry("keyword", keyword))));
        }});

        showProducts.process(request, response);
    }
}
