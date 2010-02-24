package test.com.pyxis.petstore.view;

import org.junit.Test;
import test.support.com.pyxis.petstore.builders.EntityBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.pyxis.petstore.controller.ProductsController.MATCHING_PRODUCTS_KEY;
import static com.pyxis.petstore.controller.ProductsController.SEARCH_RESULTS_VIEW;
import static com.threelevers.css.DocumentBuilder.dom;
import static org.hamcrest.MatcherAssert.assertThat;
import static test.support.com.pyxis.petstore.velocity.VelocityRendering.render;
import static test.support.com.pyxis.petstore.builders.Entities.entities;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;
import static test.support.com.pyxis.petstore.matchers.DomMatchers.hasUniqueSelector;
import static test.support.com.pyxis.petstore.matchers.DomMatchers.withText;

public class SearchProductViewTest {

    @Test
    public void displaysNumberOfProductsFound() {
        String searchResultsPage = render(SEARCH_RESULTS_VIEW).using(aModelWith(
                aProduct().withName("Dalmatian"),
                aProduct().withName("Labrador")));

        assertThat(dom(searchResultsPage), hasUniqueSelector("#match-count", withText("2")));
    }

    private static <T> Map<String, List<? super T>> aModelWith(EntityBuilder<T>... entityBuilders) {
        Map<String, List<? super T>> model = new HashMap<String, List<? super T>>();
        model.put(MATCHING_PRODUCTS_KEY, entities(entityBuilders));
        return model;
    }
}
