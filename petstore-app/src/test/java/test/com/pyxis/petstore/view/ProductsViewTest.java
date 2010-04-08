package test.com.pyxis.petstore.view;

import com.pyxis.petstore.domain.product.AttachmentStorage;
import com.pyxis.petstore.domain.product.Product;
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
import static com.threelevers.css.DocumentBuilder.dom;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;
import static test.support.com.pyxis.petstore.views.PathFor.itemsPath;
import static test.support.com.pyxis.petstore.views.PathFor.pathFor;
import static test.support.com.pyxis.petstore.views.VelocityRendering.render;

@RunWith(JMock.class)
public class ProductsViewTest {
    String PRODUCTS_VIEW = "products";
    Object DEFAULT_PHOTO_URL = "url/of/missing.png";
    String keyword = "Iguana";

    Mockery context = new JUnit4Mockery();
    AttachmentStorage attachmentStorage = context.mock(AttachmentStorage.class);
    ModelBuilder model = new ModelBuilder();
    String renderedView;

    @Before public void
    setupModel() {
        model.with("keyword", keyword);
        model.with("attachments", attachmentStorage);
    }

    @Before public void
    setUpDefaultPhoto() {
        context.checking(new Expectations() {{
            allowing(attachmentStorage).getAttachmentUrl(with(aProductWithoutPhoto())); will(returnValue(DEFAULT_PHOTO_URL));
        }});
    }

    @Test public void
    displaysNumberOfProductsFound() {
        renderedView = renderProductsView().using(model.listing(aProduct(), aProduct()));
        assertThat(dom(renderedView), hasUniqueSelector("#match-count", withText("2")));
        assertThat(dom(renderedView), hasSelector("#products tr.product", withSize(2)));
    }

    @Test public void
    displaysColumnHeadingsOnProductTable() {
        renderedView = renderProductsView().using(model.listing(aProduct()));
        assertThat(dom(renderedView),
                hasSelector("#products th",
                        inOrder(withBlankText(),
                                withText("Name"),
                                withText("Description"))));
    }

    @Test public void
    displaysProductDetailsInColumns() throws Exception {
        model.listing(aProduct().
                withNumber("LAB-1234").
                withName("Labrador").
                describedAs("Friendly").
                withPhoto("labrador.png"));
        final String photoUrl = "path/to/attachment/labrador.png";
        context.checking(new Expectations() {{
            allowing(attachmentStorage).getAttachmentUrl(with(aProductWithPhoto("labrador.png"))); will(returnValue(photoUrl));
        }});

        renderedView = renderProductsView().using(model);
        assertThat(dom(renderedView),
                hasSelector("#products td",
                        inOrder(hasChild(image(pathFor(photoUrl))),
                                productName("Labrador"),
                                description("Friendly"))));
    }

    @Test public void
    handlesProductWithNoDescriptionCorrectly() {
        renderedView = renderProductsView().using(model.listing(aProduct().withoutADescription()));
        assertThat(dom(renderedView),
                hasSelector("#products td:nth-child(3)",
                        contains(anEmptyDescription())));
    }

    @Test public void
    doesNotDisplayProductsTableIfNoProductIsFound() {
        renderedView = renderProductsView().using(model);
        assertThat(dom(renderedView), hasUniqueSelector("#no-match"));
        assertThat(dom(renderedView), hasNoSelector("#products"));
    }
    
    @Test public void
    productNameAndPhotoLinkToProductInventory() {
        renderedView = renderProductsView().using(model.listing(aProduct().withName("Labrador").withNumber("LAB-1234")));
    	assertThat(dom(renderedView),
    			hasSelector("td a", everyItem(
    					withAttribute("href", equalTo(itemsPath("LAB-1234"))))));
    }

    private Matcher<Product> aProductWithoutPhoto() {
        return aProductWithPhoto(nullValue());
    }

    private Matcher<Product> aProductWithPhoto(String photoName) {
        return aProductWithPhoto(equalTo(photoName));
    }

    private Matcher<Product> aProductWithPhoto(Matcher<? super String> photoMatcher) {
        return hasProperty("photoFileName", photoMatcher);
    }

    private Matcher<Element> anEmptyDescription() {
        return description("");
    }

    private Matcher<Element> description(String description) {
        return withText(description);
    }

    private Matcher<Element> image(String imageUrl) {
        return hasChild(withAttribute("src", equalTo(imageUrl)));
    }

    private Matcher<Element> productName(String name) {
        return withText(name);
    }

    private VelocityRendering renderProductsView() {
        return render(PRODUCTS_VIEW);
    }
}
