package test.com.pyxis.petstore.controller;

import static com.pyxis.matchers.spring.SpringMatchers.hasAttribute;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;

import java.util.Arrays;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ui.ModelMap;

import com.pyxis.petstore.controller.ProductsController;
import com.pyxis.petstore.domain.AttachmentStorage;
import com.pyxis.petstore.domain.ProductCatalog;

@RunWith(JMock.class)
public class ProductsControllerTest {

    private static final String ANY_PRODUCT = "a product";
    
    Mockery context = new JUnit4Mockery();
    ProductCatalog productCatalog = context.mock(ProductCatalog.class);
    AttachmentStorage attachmentStorage = context.mock(AttachmentStorage.class);
    ProductsController productsController = new ProductsController(productCatalog, attachmentStorage);

    @Before
    public void searchWillNotYieldAnyResult() {
        context.checking(new Expectations() {{
            allowing(productCatalog).findProductsByKeyword(ANY_PRODUCT); will(returnValue(emptyList()));
        }});
    }


    @Test public void
    listsProductsMatchingKeywordAndMakesThemAvailableToView() {

        final Object matchingProducts = Arrays.asList(aProduct().build());
        context.checking(new Expectations() {{
            oneOf(productCatalog).findProductsByKeyword("Dog"); will(returnValue(matchingProducts));
        }});

        ModelMap map = productsController.index("Dog");
        assertThat(map, hasAttribute("productList", matchingProducts));
    }

	@Test public void
    doesNotAddProductListToModelIfNoMatchIsFound() {
        ModelMap map = productsController.index(ANY_PRODUCT);
        assertThat(map, not(hasKey("productList")));
    }

    @Test public void
    makesStorageAvailableToView() {
        ModelMap map = productsController.index(ANY_PRODUCT);
        assertThat(map, hasEntry("attachments", (Object) attachmentStorage));
    }
    
}
