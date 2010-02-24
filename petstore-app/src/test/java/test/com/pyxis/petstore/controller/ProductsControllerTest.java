package test.com.pyxis.petstore.controller;

import com.pyxis.petstore.controller.ProductsController;
import com.pyxis.petstore.domain.Product;
import com.pyxis.petstore.domain.ProductCatalog;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ui.ModelMap;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(JMock.class)
public class ProductsControllerTest {

    Mockery context = new JUnit4Mockery();
    ProductCatalog productCatalog = context.mock(ProductCatalog.class);
    ProductsController productsController = new ProductsController(productCatalog);

    @Test
    public void listsProductsMatchingKeywordAndMakesThemAvailableToView() {
        final List<Product> matchingProducts = Collections.emptyList();
        context.checking(new Expectations() {{
            oneOf(productCatalog).findProductsByKeyword("Dog");
            will(returnValue(matchingProducts));
        }});

        ModelMap map = productsController.index("Dog");
        assertThat(map, hasAttribute(matchingProducts));
    }

    private Matcher<Map<? extends String, ?>> hasAttribute(List<Product> matchingProducts) {
        return Matchers.<String, Object>hasEntry("productList", matchingProducts);
    }
}
