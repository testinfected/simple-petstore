package test.unit.org.testinfected.petstore.views;

import com.github.mustachejava.TemplateFunction;
import org.testinfected.petstore.product.Product;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.w3c.dom.Element;
import test.support.org.testinfected.petstore.builders.Builder;
import test.support.org.testinfected.petstore.web.OfflineRenderer;
import test.support.org.testinfected.petstore.web.WebRoot;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.testinfected.hamcrest.dom.DomMatchers.anElement;
import static org.testinfected.hamcrest.dom.DomMatchers.hasAttribute;
import static org.testinfected.hamcrest.dom.DomMatchers.hasChild;
import static org.testinfected.hamcrest.dom.DomMatchers.hasSelector;
import static org.testinfected.hamcrest.dom.DomMatchers.hasSize;
import static org.testinfected.hamcrest.dom.DomMatchers.hasText;
import static org.testinfected.hamcrest.dom.DomMatchers.hasUniqueSelector;
import static test.support.org.testinfected.petstore.builders.Builders.build;
import static test.support.org.testinfected.petstore.builders.ProductBuilder.aProduct;
import static test.support.org.testinfected.petstore.web.OfflineRenderer.render;

public class ProductsPageTest {
    String PRODUCTS_TEMPLATE = "products";

    Element productsPage;
    List<Product> productList = new ArrayList<Product>();

    @Test public void
    indicatesWhenNoMatchWasFound() {
        productsPage = renderProductsPage().asDom();
        assertThat("products page", productsPage, hasUniqueSelector("#no-match", hasText(containsString("dog"))));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    displaysAllProductsFound() {
        addToProducts(aProduct(), aProduct());

        productsPage = renderProductsPage().with("match-count", 2).asDom();

        assertThat("products page", productsPage, hasUniqueSelector("#match-count", hasText("2")));
        assertThat("products page", productsPage, hasSelector("#catalog li[id^='product']", hasSize(2)));
    }

    @SuppressWarnings("unchecked") @Test public void
    displaysProductDetails() throws Exception {
        addToProducts(aProduct().withNumber("LAB-1234").named("Labrador").describedAs("Friendly").withPhoto("labrador.png"));

        productsPage = renderProductsPage().with("photo", new TemplateFunction() {
            public String apply(String fileName) {
                return "/photos/" + fileName;
            }
        }).asDom();

        assertThat("products page", productsPage, hasUniqueSelector("li[id='product-LAB-1234']", anElement(
                hasUniqueSelector(".product-image", hasImage("/photos/labrador.png"))),
                hasUniqueSelector(".product-name", hasText("Labrador")),
                hasUniqueSelector(".product-description", hasText("Friendly"))));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    productNameAndPhotoLinkToProductInventory() {
        addToProducts(aProduct().named("Labrador").withNumber("LAB-1234"));

        productsPage = renderProductsPage().asDom();
        assertThat("products page", productsPage, hasSelector("li a", hasSize(2)));
        assertThat("products page", productsPage, hasSelector("li a", everyItem(hasAttribute("href", equalTo("/products/LAB-1234/items")))));
    }

    private void addToProducts(Builder<Product>... products) {
        productList.addAll(build(products));
    }

    private Matcher<Element> hasImage(String imageUrl) {
        return hasChild(hasAttribute("src", equalTo(imageUrl)));
    }

    private OfflineRenderer renderProductsPage() {
        return render(PRODUCTS_TEMPLATE).
                with("products", productList).
                and("keyword", "dog").
                and("match-found", !productList.isEmpty()).from(WebRoot.pages());
    }
}
