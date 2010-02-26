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
import static org.hamcrest.Matchers.*;
import static test.support.com.pyxis.petstore.builders.Entities.entities;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;
import static test.support.com.pyxis.petstore.matchers.DomMatchers.*;
import static test.support.com.pyxis.petstore.velocity.VelocityRendering.render;

@SuppressWarnings("unchecked")
public class ProductsViewTest {
    private static final String PRODUCTS_VIEW = "products";
	private String productsPage;

	@Before
	public void setUp() {
		productsPage = render(PRODUCTS_VIEW).using(aModelWith(
				aProduct().withName("Dalmatian"),
				aProduct().withName("Labrador").describedAs("Friendly")));
	}
	
    @Test
    public void displaysNumberOfProductsFound() {
        assertThat(dom(productsPage), hasUniqueSelector("#match-count", withText("2")));
    }
    
	@Test
    public void displaysAListOfProductsFound() {
        assertThat(dom(productsPage), 
        		hasSelector("#products td", 
        				inOrder(withText("Dalmatian"), 
        						withText(""), 
        						withText("Labrador"), 
        						withText("Friendly"))));
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
