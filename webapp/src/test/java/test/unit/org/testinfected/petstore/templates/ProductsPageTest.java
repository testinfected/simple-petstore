package test.unit.org.testinfected.petstore.templates;

import com.pyxis.petstore.domain.product.Product;
import org.junit.Test;
import org.w3c.dom.Element;
import test.support.org.testinfected.petstore.web.OfflineRenderer;
import test.support.org.testinfected.petstore.web.WebRoot;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testinfected.hamcrest.dom.DomMatchers.anElement;
import static org.testinfected.hamcrest.dom.DomMatchers.hasBlankText;
import static org.testinfected.hamcrest.dom.DomMatchers.hasSelector;
import static org.testinfected.hamcrest.dom.DomMatchers.hasSize;
import static org.testinfected.hamcrest.dom.DomMatchers.hasText;
import static org.testinfected.hamcrest.dom.DomMatchers.hasUniqueSelector;
import static org.testinfected.petstore.util.ContextBuilder.context;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;

public class ProductsPageTest {
    String PRODUCTS_TEMPLATE = "products";

    Element productsPage;

    @Test public void
    displaysAllProductsFound() {
        List<Product> productList = asList(
                aProduct().named("Labrador").build(), 
                aProduct().named("Pug").build());

        productsPage = renderProductsPage().using(context().
                with("products", productList).
                with("matchCount", 2).
                with("keyword", "dog")).asDom();

        assertThat("products page", productsPage, hasUniqueSelector("#match-count", hasText("2")));
        assertThat("products page", productsPage, hasSelector("#catalog li[id^='product']", hasSize(2)));
    }

    @SuppressWarnings("unchecked") @Test public void
    displaysProductDetails() throws Exception {
        List<Product> productList = asList(
                aProduct().withNumber("LAB-1234").named("Labrador").describedAs("Friendly").build());

        productsPage = renderProductsPage().using(context().
                with("products", productList).
                with("productCount", 1).
                with("keyword", "dog")).asDom();

        assertThat("products page", productsPage, hasUniqueSelector("li[id='product-LAB-1234']", anElement(
                hasUniqueSelector(".product-name", hasText("Labrador")),
                hasUniqueSelector(".product-description", hasText("Friendly")))));
    }

    @SuppressWarnings("unchecked") @Test public void
    handlesProductWithNoDescription() {
        List<Product> productList = asList(aProduct().withNoDescription().build());

        productsPage = renderProductsPage().using(context().
                with("products", productList).
                with("productCount", 1).
                with("keyword", "dog")).asDom();

        assertThat("products page", productsPage,
                hasSelector(".product-description", hasBlankText()));
    }

    private OfflineRenderer renderProductsPage() {
        return OfflineRenderer.render(PRODUCTS_TEMPLATE).from(WebRoot.locatePages());
    }
}
