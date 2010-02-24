package test.com.pyxis.petstore.view;

import org.junit.Test;
import org.springframework.ui.ModelMap;
import test.support.com.pyxis.petstore.builders.EntityBuilder;

import java.util.Map;

import static com.threelevers.css.DocumentBuilder.dom;
import static org.hamcrest.MatcherAssert.assertThat;
import static test.support.com.pyxis.petstore.builders.Entities.entities;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;
import static test.support.com.pyxis.petstore.matchers.DomMatchers.hasUniqueSelector;
import static test.support.com.pyxis.petstore.matchers.DomMatchers.withText;
import static test.support.com.pyxis.petstore.velocity.VelocityRendering.render;

public class ProductsViewTest {
    private static final String PRODUCTS_VIEW = "products";

    @Test
    public void displaysNumberOfProductsFound() {
        String searchResultsPage = render(PRODUCTS_VIEW).using(aModelWith(
                aProduct().withName("Dalmatian"),
                aProduct().withName("Labrador")));

        assertThat(dom(searchResultsPage), hasUniqueSelector("#match-count", withText("2")));
    }

    private static Map<String, ?> aModelWith(EntityBuilder<?>... entityBuilders) {
        ModelMap model = new ModelMap();
        model.addAttribute(entities(entityBuilders));
        return model;
    }
}
