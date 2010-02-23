package test.com.pyxis.petstore.view;

import static com.pyxis.petstore.controller.ItemsController.MATCHING_ITEMS_KEY;
import static com.pyxis.petstore.controller.ItemsController.SEARCH_RESULTS_VIEW_NAME;
import static com.threelevers.css.DocumentBuilder.doc;
import static com.threelevers.css.Selector.from;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static test.support.com.pyxis.petstore.matchers.HasSelector.hasSelector;
import static test.support.com.pyxis.petstore.matchers.WithContentText.withText;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.app.VelocityEngine;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;

import com.pyxis.petstore.domain.Item;
import org.w3c.dom.Element;

import static test.support.com.pyxis.petstore.matchers.DomMatchers.*;
import static test.support.com.pyxis.petstore.matchers.WithContentText.withText;

public class SearchItemViewTest {

	private static final String VELOCITY_EXTENSION = ".vm";

	VelocityEngine velocityEngine;

	@Before
	public void setUp() throws Exception
	{
		VelocityConfigurer velocityConfigurer = new VelocityConfigurer();
		Properties viewsProperties = new Properties();
		viewsProperties.load(SearchItemViewTest.class.getResourceAsStream("/views.properties"));
		String templatesBaseUrl = viewsProperties.getProperty("templates.base.url");
		velocityConfigurer.setResourceLoaderPath(templatesBaseUrl);
		velocityConfigurer.afterPropertiesSet();
		this.velocityEngine = velocityConfigurer.getVelocityEngine();
	}

	@Test
	public void shouldDisplayNamesOfItemsFound()
	{
		String searchResultsPage = renderSearchResultsWith(aModelWith(
				new Item("Dalmatian"),
				new Item("Labrador")));
        assertThat(dom(searchResultsPage), hasUniqueSelector("#match-count", withText("2")));
	}

    private Element dom(String d) {
        return doc(d).getDocumentElement();
    }

    private Map<String, Object> aModelWith(Item... items) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put(MATCHING_ITEMS_KEY, Arrays.asList(items));
		return model;
	}

	private String renderSearchResultsWith(Map<String, Object> model) {
		return VelocityEngineUtils.mergeTemplateIntoString(this.velocityEngine, searchResultsTemplate(), model);
	}

	private String searchResultsTemplate() {
		return SEARCH_RESULTS_VIEW_NAME + VELOCITY_EXTENSION;
	}

}
