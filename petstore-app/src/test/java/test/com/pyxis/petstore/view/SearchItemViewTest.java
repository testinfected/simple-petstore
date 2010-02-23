package test.com.pyxis.petstore.view;

import org.junit.Test;
import test.support.com.pyxis.petstore.builders.EntityBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.pyxis.petstore.controller.ItemsController.MATCHING_ITEMS_KEY;
import static com.pyxis.petstore.controller.ItemsController.SEARCH_RESULTS_VIEW;
import static com.threelevers.css.DocumentBuilder.dom;
import static org.hamcrest.MatcherAssert.assertThat;
import static test.com.pyxis.petstore.view.VelocityRendering.render;
import static test.support.com.pyxis.petstore.builders.Entities.entities;
import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;
import static test.support.com.pyxis.petstore.matchers.DomMatchers.hasUniqueSelector;
import static test.support.com.pyxis.petstore.matchers.DomMatchers.withText;

public class SearchItemViewTest {

    @Test
    public void shouldDisplayNamesOfItemsFound() {
        String searchResultsPage = render(SEARCH_RESULTS_VIEW).using(aModelWith(
                anItem().withName("Dalmatian"),
                anItem().withName("Labrador")));

        assertThat(dom(searchResultsPage), hasUniqueSelector("#match-count", withText("2")));
    }

    private static <T> Map<String, List<? super T>> aModelWith(EntityBuilder<T>... entityBuilders) {
        Map<String, List<? super T>> model = new HashMap<String, List<? super T>>();
        model.put(MATCHING_ITEMS_KEY, entities(entityBuilders));
        return model;
    }
}
