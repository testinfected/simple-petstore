package test.com.pyxis.petstore.view;

import com.pyxis.petstore.domain.Storage;
import com.pyxis.petstore.domain.Product;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ui.ModelMap;
import org.w3c.dom.Element;
import test.support.com.pyxis.petstore.builders.EntityBuilder;

import java.util.Map;

import static com.threelevers.css.DocumentBuilder.dom;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static test.support.com.pyxis.petstore.builders.Entities.entities;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;
import static test.support.com.pyxis.petstore.matchers.DomMatchers.*;
import static test.support.com.pyxis.petstore.velocity.VelocityRendering.render;

@RunWith(JMock.class)
public class ProductsViewTest {
    private static final String PRODUCTS_VIEW = "products";
    private static final String DEFAULT_PHOTO = "/path/to/missing.png";

    Mockery context = new JUnit4Mockery();
    Storage storage = context.mock(Storage.class);
    String productsPage;

    @Before public void
    allowDefaultPhoto() {
        context.checking(new Expectations() {{
            allowing(storage).getLocation(with(Product.MISSING_PHOTO_URL)); will(returnValue(DEFAULT_PHOTO));
        }});
    }

    @Test public void
    displaysNumberOfProductsFound() {
        productsPage = renderProductsPageUsing(aModelWith(aProduct(), aProduct()));
        assertThat(dom(productsPage), hasUniqueSelector("#match-count", withText("2")));
    }

    @Test public void
    displaysColumnHeadingsOfProductTable() {
        productsPage = renderProductsPageUsing(aModelWith(aProduct()));
        assertThat(dom(productsPage),
                hasSelector("#products th",
                        inOrder(withEmptyText(),
                                withText("Name"),
                                withText("Description"))));
    }

    @Test public void
    displaysProductDetailsInColumns() throws Exception {
        Map<String, ?> model = aModelWith(aProduct().withName("Labrador").describedAs("Friendly").withPhotoUri("/labrador.png"));
        final String imageUrl = "/relative/path/to/attachment/labrador.png";
        context.checking(new Expectations() {{
            allowing(storage).getLocation("/labrador.png"); will(returnValue(imageUrl));
        }});

        productsPage = renderProductsPageUsing(model);
        assertThat(dom(productsPage),
                hasSelector("#products td",
                        inOrder(image(imageUrl),
                                productName("Labrador"),
                                description("Friendly"))));
    }

    @Test public void
    handlesProductWithNoDescriptionCorrectly() {
        productsPage = renderProductsPageUsing(aModelWith(aProduct().withName("Labrador")));
        assertThat(dom(productsPage),
                hasSelector("#products td:nth-child(3)",
                        contains(anEmptyDescription())));
    }

    @Test public void
    displaysNoProductsTableIfNoProductIsFound() {
        productsPage = renderProductsPageUsing(anEmptyModel());
        assertThat(dom(productsPage), hasUniqueSelector("#no-match"));
        assertThat(dom(productsPage), hasNoSelector("#products"));
    }

    private Map<String, ?> anEmptyModel() {
        return aModelWith();
    }

    private String renderProductsPageUsing(Map<String, ?> model) {
        return render(PRODUCTS_VIEW).using(model);
    }

    private Matcher<Element> withEmptyText() {
        return withText("");
    }

    private Matcher<Element> anEmptyDescription() {
        return description("");
    }

    private Matcher<Element> image(String imageUrl) {
        return hasChild(hasAttribute("src", equalTo(imageUrl)));
    }

    private Matcher<Element> description(String description) {
        return withText(description);
    }

    private Matcher<Element> productName(String name) {
        return withText(name);
    }

    private Matcher<Iterable<Element>> inOrder(Matcher<Element>... elementMatchers) {
        return contains(elementMatchers);
    }

    private Map<String, ?> aModelWith(EntityBuilder<?>... entityBuilders) {
        ModelMap model = new ModelMap();
        model.addAttribute(storage);
        model.addAttribute(entities(entityBuilders));
        return model;
    }
}
