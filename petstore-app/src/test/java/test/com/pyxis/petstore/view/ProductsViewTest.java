package test.com.pyxis.petstore.view;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ModelMap;
import org.w3c.dom.Element;
import test.support.com.pyxis.petstore.builders.EntityBuilder;

import java.util.Map;

import static com.threelevers.css.DocumentBuilder.dom;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static test.support.com.pyxis.petstore.builders.Entities.entities;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;
import static test.support.com.pyxis.petstore.matchers.DomMatchers.*;
import static test.support.com.pyxis.petstore.velocity.VelocityRendering.render;

public class ProductsViewTest {
    private static final String PRODUCTS_VIEW = "products";
	private String searchResultsPage;

	@Before
	public void setUp() {
		searchResultsPage = render(PRODUCTS_VIEW).using(aModelWith(
				aProduct().withName("Dalmatian"),
				aProduct().withName("Labrador")));
	}
	
    @Test
    public void displaysNumberOfProductsFound() {
        assertThat(dom(searchResultsPage), hasUniqueSelector("#match-count", withText("2")));
    }
    
    @Test
    public void displaysAListOfProductsFound() {
        assertThat(dom(searchResultsPage), hasSelector("#products li", inOrder(withText("Dalmatian"), withText("Labrador"))));
    }

    private Matcher<Iterable<Element>> inOrder(Matcher<Element>... elementMatchers) {
        return contains(elementMatchers);
    }

    private static Map<String, ?> aModelWith(EntityBuilder<?>... entityBuilders) {
        ModelMap model = new ModelMap();
        model.addAttribute(entities(entityBuilders));
        return model;
    }
}
