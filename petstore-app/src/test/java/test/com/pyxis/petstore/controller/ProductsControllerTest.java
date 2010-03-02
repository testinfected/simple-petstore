package test.com.pyxis.petstore.controller;

import com.pyxis.petstore.controller.ProductsController;
import com.pyxis.petstore.domain.ProductCatalog;
import com.pyxis.petstore.domain.Storage;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ui.ModelMap;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;

@RunWith(JMock.class)
public class ProductsControllerTest {

    private static final String ANY_PRODUCT = "a product";
    
    Mockery context = new JUnit4Mockery();
    ProductCatalog productCatalog = context.mock(ProductCatalog.class);
    Storage storage = context.mock(Storage.class);
    ProductsController productsController = new ProductsController(productCatalog, storage);

    @Test public void
    listsProductsMatchingKeywordAndMakesThemAvailableToView() {
        final Object matchingProducts = Arrays.asList(aProduct().build());
        context.checking(new Expectations() {{
            oneOf(productCatalog).findProductsByKeyword("Dog"); will(returnValue(matchingProducts));
        }});

        ModelMap map = productsController.index("Dog");
        assertThat(map, hasEntry("productList", matchingProducts));
    }

    @Test public void
    doesNotAddProductListToModelIfNoMatchIsFound() {
        ModelMap map = productsController.index(ANY_PRODUCT);
        assertThat(map, not(hasKey("productList")));
    }

    @Before
    public void searchWillNotYieldAnyResult() {
        context.checking(new Expectations() {{
            allowing(productCatalog).findProductsByKeyword(ANY_PRODUCT); will(returnValue(Collections.emptyList()));
        }});
    }

    @Test public void
    makesStorageAvailableToView() {
        ModelMap map = productsController.index(ANY_PRODUCT);
        assertThat(map, hasEntry("storage", (Object) storage));
    }
}
