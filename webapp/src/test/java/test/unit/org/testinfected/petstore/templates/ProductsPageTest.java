package test.unit.org.testinfected.petstore.templates;

import org.junit.Test;
import org.w3c.dom.Element;
import test.support.org.testinfected.petstore.web.OfflineRenderer;
import test.support.org.testinfected.petstore.web.WebRoot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.testinfected.hamcrest.dom.DomMatchers.hasUniqueSelector;

public class ProductsPageTest {
    String PRODUCTS_PAGE_TEMPLATE = "pages/products";

    Element productPage;

    @Test public void
    doesNotDisplayProductListWhenNoProductIsFound() {
        productPage = renderProductsPage().asDom();
        assertThat("products page", productPage, hasUniqueSelector("#no-match"));
    }

    private OfflineRenderer renderProductsPage() {
        return OfflineRenderer.render(PRODUCTS_PAGE_TEMPLATE).from(WebRoot.locate());
    }
}
