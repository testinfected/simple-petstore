package test.unit.org.testinfected.petstore.views;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.w3c.dom.Element;
import test.support.org.testinfected.petstore.web.OfflineRenderer;
import test.support.org.testinfected.petstore.web.WebRoot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.testinfected.hamcrest.dom.DomMatchers.anElement;
import static org.testinfected.hamcrest.dom.DomMatchers.hasAttribute;
import static org.testinfected.hamcrest.dom.DomMatchers.hasChildren;
import static org.testinfected.hamcrest.dom.DomMatchers.hasId;
import static org.testinfected.hamcrest.dom.DomMatchers.hasNoSelector;
import static org.testinfected.hamcrest.dom.DomMatchers.hasTag;
import static org.testinfected.hamcrest.dom.DomMatchers.hasText;
import static org.testinfected.hamcrest.dom.DomMatchers.hasUniqueSelector;
import static test.support.com.pyxis.petstore.builders.CartBuilder.aCart;
import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;
import static test.support.org.testinfected.petstore.web.OfflineRenderer.render;

public class HeaderTest {

    String HEADER_TEMPLATE = "header";
    Element header;

    @Test public void
    hasAClickableLogoThatReturnsToTheHomePage() {
        header = renderHeader().asDom();
        assertThat("content", header, hasUniqueSelector("#logo a", hasAttribute("href", "/")));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    containsASearchBoxToQueryTheProductCatalog() {
        header = renderHeader().asDom();
        assertThat("header", header,
                hasUniqueSelector("#search-box form",
                        hasAttribute("action", "/products"),
                        hasAttribute("method", equalToIgnoringCase("GET"))));
        assertThat("header", header,
                hasUniqueSelector("#search-box form", hasChildren(keywordInputField(), searchButton())));
    }

    @Test public void
    cartLinkIsInactiveWhenCartIsEmpty() {
        header = renderHeader().with("cart", aCart()).asDom();
        assertThat("header", header, hasNoSelector("#shopping-cart a"));
        assertThat("header", header, hasText(containsString("0")));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    displaysTotalItemsInCartAndLinksToCart() throws Exception {
        header = renderHeader().with("cart", aCart().containing(anItem(), anItem())).asDom();
        assertThat("header", header, hasUniqueSelector("#shopping-cart a",
                        hasAttribute("href", "/cart"),
                        hasText(containsString("2"))));
    }

    @SuppressWarnings("unchecked")
    private Matcher<Element> keywordInputField() {
        return anElement(hasTag("input"), hasId("keyword"), hasAttribute("type", "text"), hasAttribute("name", "keyword"));
    }

    @SuppressWarnings("unchecked")
    private Matcher<Element> searchButton() {
        return anElement(hasTag("button"), hasId("search"));
    }

    private OfflineRenderer renderHeader() {
        return render(HEADER_TEMPLATE).from(WebRoot.layouts());
    }
}
