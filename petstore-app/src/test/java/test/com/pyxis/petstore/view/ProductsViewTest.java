package test.com.pyxis.petstore.view;

import static com.threelevers.css.DocumentBuilder.dom;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static test.support.com.pyxis.petstore.builders.Entities.entities;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;
import static test.support.com.pyxis.petstore.matchers.DomMatchers.hasAttribute;
import static test.support.com.pyxis.petstore.matchers.DomMatchers.hasChild;
import static test.support.com.pyxis.petstore.matchers.DomMatchers.hasSelector;
import static test.support.com.pyxis.petstore.matchers.DomMatchers.hasUniqueSelector;
import static test.support.com.pyxis.petstore.matchers.DomMatchers.withText;
import static test.support.com.pyxis.petstore.velocity.VelocityRendering.render;

import java.util.Map;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.springframework.ui.ModelMap;
import org.w3c.dom.Element;

import test.support.com.pyxis.petstore.builders.EntityBuilder;

@SuppressWarnings("unchecked")
public class ProductsViewTest {
    private static final String PRODUCTS_VIEW = "products";
	private String productsPage;

    @Test
    public void displaysNumberOfProductsFound() {
    	productsPage = renderProductsPageUsing(aModelWith(
    			aProduct().withName("Dalmatian"),
    			aProduct().withName("Labrador").describedAs("Friendly").withPhoto("labrador")));
        assertThat(dom(productsPage), hasUniqueSelector("#match-count", withText("2")));
    }
    
    @Test
    public void displaysColumnHeadersOverListOfProductsFound() {
		productsPage = renderProductsPageUsing(aModelWith(
				aProduct().withName("Labrador").describedAs("Friendly").withPhoto("labrador")));
        assertThat(dom(productsPage), 
        		hasSelector("#products th", 
        				inOrder(withEmptyText(),
        						withText("Name"),
        						withText("Description"))));
    }

	@Test
    public void displaysNameDescriptionAndPhotoOfProductsFoundInDifferentColumns() {
		productsPage = renderProductsPageUsing(aModelWith(
				aProduct().withName("Labrador").describedAs("Friendly").withPhoto("labrador")));
        assertThat(dom(productsPage), 
        		hasSelector("#products td", 
        				inOrder(image("photos/labrador"),
        						productName("Labrador"), 
        						description("Friendly"))));
    }
	
	@Test
	public void displaysNothingInDescriptionColumnForAProductWithNoDescription() {
		productsPage = renderProductsPageUsing(aModelWith(
				aProduct().withName("Labrador").withPhoto("labrador")));
        assertThat(dom(productsPage), 
        		hasSelector("#products td:nth-child(3)", 
        				contains(anEmptyDescription())));		
	}
	
	@Test
	public void displaysDefaultPhotoForAProductWithNoPhoto() {
		productsPage = renderProductsPageUsing(aModelWith(
						aProduct().withName("Labrador").describedAs("Friendly")));
        assertThat(dom(productsPage), 
        		hasSelector("#products td:nth-child(1)", 
        				contains(image("photos/"))));		
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

    private static Map<String, ?> aModelWith(EntityBuilder<?>... entityBuilders) {
        ModelMap model = new ModelMap();
        model.addAttribute(entities(entityBuilders));
        return model;
    }
}
