package test.unit.org.testinfected.petstore.views;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.testinfected.petstore.views.PlainPage;
import org.w3c.dom.Element;
import test.support.org.testinfected.petstore.web.OfflineRenderer;
import test.support.org.testinfected.petstore.web.WebRoot;

import java.util.HashMap;
import java.util.Map;

import static com.vtence.hamcrest.dom.DomMatchers.anElement;
import static com.vtence.hamcrest.dom.DomMatchers.hasAttribute;
import static com.vtence.hamcrest.dom.DomMatchers.hasChild;
import static com.vtence.hamcrest.dom.DomMatchers.hasChildren;
import static com.vtence.hamcrest.dom.DomMatchers.hasClassName;
import static com.vtence.hamcrest.dom.DomMatchers.hasId;
import static com.vtence.hamcrest.dom.DomMatchers.hasNoSelector;
import static com.vtence.hamcrest.dom.DomMatchers.hasText;
import static com.vtence.hamcrest.dom.DomMatchers.hasUniqueSelector;
import static com.vtence.hamcrest.dom.HasTag.hasTag;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static test.support.org.testinfected.petstore.builders.CartBuilder.aCart;
import static test.support.org.testinfected.petstore.builders.ItemBuilder.anItem;
import static test.support.org.testinfected.petstore.web.OfflineRenderer.render;

public class HeaderTest {

    String HEADER_TEMPLATE = "header";
    Element header;
    PlainPage page = new PlainPage();

    @Test public void
    logoReturnsToTheHomePage() {
        header = renderHeader().with(page).asDom();
        assertThat("content", header, hasUniqueSelector("#logo a", hasAttribute("href", "/")));
    }

    @Test public void
    searchBoxQueriesTheProductCatalog() {
        header = renderHeader().with(page).asDom();
        assertThat("header", header,
                hasUniqueSelector("#search-box form",
                        hasAttribute("action", "/products"),
                        hasAttribute("method", equalToIgnoringCase("GET"))));
        assertThat("header", header,
                hasUniqueSelector("#search-box form", hasChildren(keywordInputField(), searchButton())));
    }

    @Test public void
    cartLinkIsInactiveWhenCartIsEmpty() {
        header = renderHeader().with(page.withCart(aCart().build())).asDom();
        assertThat("header", header, hasNoSelector("#shopping-cart a"));
        assertThat("header", header, hasText(containsString("0")));
    }

    @Test public void
    displaysTotalItemsInCartAndLinksToCart() throws Exception {
        header = renderHeader().with(page.withCart(aCart().containing(anItem(),
                anItem()).build())).asDom();
        assertThat("header", header, hasUniqueSelector("#shopping-cart a",
                hasAttribute("href", "/cart"),
                hasText(containsString("2"))));
    }

    @Test public void
    linksToHomePage() {
        header = renderHeader().with(page).asDom();
        assertThat("header", header, hasUniqueSelector("#home a", hasAttribute("href", "/")));
    }

    @Test public void
    indicatesWhenDisplayingCartContent() {
        Map<String, String> data = new HashMap<>();
        data.put("section", "cart");
        header = renderHeader().with(page.composedOf(data).withCart(aCart().build())).asDom();
        assertThat("header", header, allOf(
                hasUniqueSelector("#home", hasClassName("overline"), hasClassName("cart")),
                hasUniqueSelector("#shopping-cart", hasClassName("overline"), hasClassName("cart")),
                hasUniqueSelector("#tab.cart", hasChild(allOf(hasTag("img"), hasAttribute("src", "/images/tab.png"))))));
    }

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
