package test.unit.org.testinfected.petstore.templates;

import com.pyxis.petstore.domain.product.Product;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.testinfected.petstore.endpoints.ShowProducts;
import org.testinfected.petstore.util.ContextBuilder;
import org.w3c.dom.Element;
import test.support.com.pyxis.petstore.builders.Builder;
import test.support.org.testinfected.petstore.web.OfflineRenderer;
import test.support.org.testinfected.petstore.web.Paths;
import test.support.org.testinfected.petstore.web.WebRoot;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.testinfected.hamcrest.dom.DomMatchers.anElement;
import static org.testinfected.hamcrest.dom.DomMatchers.hasAttribute;
import static org.testinfected.hamcrest.dom.DomMatchers.hasChild;
import static org.testinfected.hamcrest.dom.DomMatchers.hasSelector;
import static org.testinfected.hamcrest.dom.DomMatchers.hasSize;
import static org.testinfected.hamcrest.dom.DomMatchers.hasText;
import static org.testinfected.hamcrest.dom.DomMatchers.hasUniqueSelector;
import static org.testinfected.petstore.util.ContextBuilder.context;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;

public class ProductsPageTest {
    String PRODUCTS_TEMPLATE = "products";

    Element productsPage;
    Paths paths = Paths.root();
    List<ShowProducts.ProductAndPhoto> productList = new ArrayList<ShowProducts.ProductAndPhoto>();
    ContextBuilder context = context().with("products", productList).with("keyword", "dog");

    @Test public void
    displaysAllProductsFound() {
        addToProducts(productWithPhoto(aProduct()));
        addToProducts(productWithPhoto(aProduct()));

        productsPage = renderProductsPage().using(context.with("matchCount", 2)).asDom();

        assertThat("products page", productsPage, hasUniqueSelector("#match-count", hasText("2")));
        assertThat("products page", productsPage, hasSelector("#catalog li[id^='product']", hasSize(2)));
    }

    @SuppressWarnings("unchecked") @Test public void
    displaysProductDetails() throws Exception {
        addToProducts(productWithPhoto(
                aProduct().withNumber("LAB-1234").named("Labrador").describedAs("Friendly"), "/photos/labrador.png"));

        productsPage = renderProductsPage().using(context).asDom();

        assertThat("products page", productsPage, hasUniqueSelector("li[id='product-LAB-1234']", anElement(
                hasUniqueSelector(".product-link", hasImage(paths.pathFor("/photos/labrador.png"))),
                hasUniqueSelector(".product-name", hasText("Labrador")),
                hasUniqueSelector(".product-description", hasText("Friendly")))));
    }

    private void addToProducts(final ShowProducts.ProductAndPhoto productAndPhoto) {
        productList.add(productAndPhoto);
    }

    private ShowProducts.ProductAndPhoto productWithPhoto(Builder<Product> product) {
        return productWithPhoto(product, "photo.png");
    }

    private ShowProducts.ProductAndPhoto productWithPhoto(Builder<Product> product, String photo) {
        return new ShowProducts.ProductAndPhoto(product.build(), photo);
    }

    private Matcher<Element> hasImage(String imageUrl) {
        return hasChild(hasAttribute("src", equalTo(imageUrl)));
    }

    private OfflineRenderer renderProductsPage() {
        return OfflineRenderer.render(PRODUCTS_TEMPLATE).from(WebRoot.locatePages());
    }
}
