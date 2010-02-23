package test.com.pyxis.petstore.view;

import static com.pyxis.petstore.controller.ItemsController.MATCHING_ITEMS_KEY;
import static com.pyxis.petstore.controller.ItemsController.SEARCH_RESULTS_VIEW;
import static com.threelevers.css.DocumentBuilder.doc;
import static com.threelevers.css.Selector.from;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static test.com.pyxis.petstore.view.VelocityRendering.render;
import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import test.support.com.pyxis.petstore.builders.ItemBuilder;

import com.pyxis.petstore.domain.Item;

public class SearchItemViewTest {

	@Test
	public void shouldDisplayNamesOfItemsFound()
	{
		String searchResultsPage = render(SEARCH_RESULTS_VIEW).using(aModelWith(
										anItem().withName("Dalmatian"),
										anItem().withName("Labrador")));
        assertThat(from(doc(searchResultsPage)).selectUnique("#match-count").getTextContent(), is(equalTo("2")));
	}

	public static Map<String, Object> aModelWith(ItemBuilder... itemBuilders) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put(MATCHING_ITEMS_KEY, items(itemBuilders));
		return model;
	}

	private static List<Item> items(ItemBuilder... itemBuilders) {
		List<Item> items = new ArrayList<Item>();
		for (ItemBuilder itemBuilder : itemBuilders) {
			items.add(itemBuilder.build());
		}
		return items;
	}

}
