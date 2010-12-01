package test.com.pyxis.petstore.view;

import com.pyxis.petstore.domain.product.AttachmentStorage;
import com.pyxis.petstore.domain.product.Product;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Element;
import test.support.com.pyxis.petstore.views.ModelBuilder;
import test.support.com.pyxis.petstore.views.VelocityRendering;

import static com.pyxis.matchers.dom.DomMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;
import static test.support.com.pyxis.petstore.views.PathFor.itemsPath;
import static test.support.com.pyxis.petstore.views.PathFor.pathFor;
import static test.support.com.pyxis.petstore.views.VelocityRendering.render;

@RunWith(JMock.class)
public class ProductsViewTest {
    String PRODUCTS_VIEW_NAME = "products";
    Object DEFAULT_PHOTO_URL = "url/of/missing.png";
    String keyword = "Iguana";

    Mockery context = new JUnit4Mockery();
    AttachmentStorage attachmentStorage = context.mock(AttachmentStorage.class);
    ModelBuilder model = new ModelBuilder();
    Element productsView;

    @Before public void
    setupModel() {
        model.with("keyword", keyword);
        model.with("attachments", attachmentStorage);
    }

    @Before public void
    setUpDefaultPhoto() {
        context.checking(new Expectations() {{
            allowing(attachmentStorage).getAttachmentUrl(with(aProductWithNoPhoto())); will(returnValue(DEFAULT_PHOTO_URL));
        }});
    }

    @Test public void
    displaysNumberOfProductsFound() {
        productsView = renderProductsView().using(model.listing(aProduct(), aProduct())).asDom();
        assertThat("view", productsView, hasUniqueSelector("#match-count", withText("2")));
        assertThat("view", productsView, hasSelector("#catalog li[id^='product']", withSize(2)));
    }

    @Test public void
    displaysProductDetails() throws Exception {
        model.listing(aProduct().
                withNumber("LAB-1234").
                withName("Labrador").
                describedAs("Friendly").
                withPhoto("labrador.png"));
        final String photoUrl = "path/to/attachment/labrador.png";
        context.checking(new Expectations() {{
            allowing(attachmentStorage).getAttachmentUrl(with(aProductWithPhoto("labrador.png"))); will(returnValue(photoUrl));
        }});

        productsView = renderProductsView().using(model).asDom();
        assertThat("view", productsView,
                hasSelector(".product-link", contains(anImage(pathFor(photoUrl)))));
        assertThat("view", productsView,
                hasSelector(".product-name", contains(withText("Labrador"))));
        assertThat("view", productsView,
                hasSelector(".product-description", contains(withText("Friendly"))));
    }

    @Test public void
    handlesProductWithNoDescriptionCorrectly() {
        productsView = renderProductsView().using(model.listing(aProduct().withNoDescription())).asDom();
        assertThat("view", productsView,
                hasSelector(".product-description",
                        contains(anEmptyDescription())));
    }

    @Test public void
    doesNotDisplayProductsIfNoProductIsFound() {
        productsView = renderProductsView().using(model).asDom();
        assertThat("view", productsView, hasUniqueSelector("#no-match"));
        assertThat("view", productsView, hasNoSelector("#catalog li"));
    }
    
    @Test public void
    productNameAndPhotoLinkToProductInventory() {
        productsView = renderProductsView().using(model.listing(aProduct().withName("Labrador").withNumber("LAB-1234"))).asDom();
    	assertThat("view", productsView,
    			hasSelector("li a", everyItem(
    					withAttribute("href", equalTo(itemsPath("LAB-1234"))))));
    }

    private Matcher<Product> aProductWithNoPhoto() {
        return aProductWithPhoto(nullValue());
    }

    private Matcher<Product> aProductWithPhoto(String photoName) {
        return aProductWithPhoto(equalTo(photoName));
    }

    private Matcher<Product> aProductWithPhoto(Matcher<? super String> photoMatcher) {
        return new FeatureMatcher<Product, String>(photoMatcher, "a product with photo filename", "photo filename") {
            @Override protected String featureValueOf(Product actual) {
                return actual.getPhotoFileName();
            }
        };
    }

    private Matcher<Element> anEmptyDescription() {
        return withBlankText();
    }

    private Matcher<Element> anImage(String imageUrl) {
        return hasChild(withAttribute("src", equalTo(imageUrl)));
    }

    private VelocityRendering renderProductsView() {
        return render(PRODUCTS_VIEW_NAME);
    }
}
