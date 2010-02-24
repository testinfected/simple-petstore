package test.com.pyxis.petstore.controller;

import com.pyxis.petstore.controller.ProductsController;
import com.pyxis.petstore.domain.Product;
import com.pyxis.petstore.domain.ProductCatalog;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.ModelAndViewAssert.assertModelAttributeValue;

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

        ModelAndView view = productsController.index("Dog");
        assertModelAttributeValue(view, "matchingProducts", matchingProducts);
        assertThat(view.getViewName(), is("products/index"));
    }
}
