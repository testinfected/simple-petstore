package test.com.pyxis.petstore.controller;

import com.pyxis.petstore.controller.ProductsController;
import com.pyxis.petstore.domain.AttachmentStorage;
import com.pyxis.petstore.domain.ProductCatalog;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Map;

import static com.pyxis.matchers.spring.SpringMatchers.hasAttribute;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;

@RunWith(JMock.class)
public class ProductsControllerTest {

    String ANY_PRODUCT = "a product";
    
    Mockery context = new JUnit4Mockery();
    ProductCatalog productCatalog = context.mock(ProductCatalog.class);
    AttachmentStorage attachmentStorage = context.mock(AttachmentStorage.class);
    ProductsController productsController = new ProductsController(productCatalog, attachmentStorage);

    Map<String, ?> model;

    @Before public void
    searchWillNotYieldAnyResult() {
        context.checking(new Expectations() {{
            allowing(productCatalog).findByKeyword(ANY_PRODUCT); will(returnValue(emptyList()));
        }});
    }

    @Test public void
    listsProductsMatchingKeywordAndMakesThemAvailableToView() {
        final Object matchingProducts = Arrays.asList(aProduct().build());
        context.checking(new Expectations() {{
            oneOf(productCatalog).findByKeyword("Dog"); will(returnValue(matchingProducts));
        }});

        model = productsController.index("Dog");
        assertThat(model, hasAttribute("productList", matchingProducts));
    }

	@Test public void
    doesNotAddProductListToModelIfNoMatchIsFound() {
        model = productsController.index(ANY_PRODUCT);
        assertThat(model, not(hasKey("productList")));
    }

    @Test public void
    makesStorageAvailableToView() {
        AttachmentStorage storage = productsController.getAttachmentStorage();
        assertThat(storage, sameInstance(attachmentStorage));
    }
    
    @Test public void
    makesSearchKeywordAvailableToView() {
        model = productsController.index(ANY_PRODUCT);
        assertThat(model , hasAttribute("keyword", ANY_PRODUCT));
    }
}
