package test.unit.org.testinfected.petstore.templates;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import test.support.org.testinfected.petstore.web.Paths;
import test.support.org.testinfected.petstore.web.WebRoot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.testinfected.hamcrest.dom.DomMatchers.anElement;
import static org.testinfected.hamcrest.dom.DomMatchers.hasAttribute;
import static org.testinfected.hamcrest.dom.DomMatchers.hasChildren;
import static org.testinfected.hamcrest.dom.DomMatchers.hasId;
import static org.testinfected.hamcrest.dom.DomMatchers.hasTag;
import static org.testinfected.hamcrest.dom.DomMatchers.hasUniqueSelector;
import static test.support.org.testinfected.petstore.web.OfflineRenderer.render;

public class HeaderTest {

    String HEADER_TEMPLATE = "header";
    Element header;
    Paths paths = Paths.root();

    @Before public void
    renderHeader() {
        header = render(HEADER_TEMPLATE).from(WebRoot.locate().layouts).asDom();
    }

    @Test public void
    hasAClickableLogoThatReturnsToTheHomePage() {
        assertThat("content", header, hasUniqueSelector("#logo a", hasAttribute("href", "/")));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    containsASearchBoxToQueryTheProductCatalog() {
        assertThat("header", header,
                hasUniqueSelector("#search-box form",
                        hasAttribute("action", paths.productsPath()),
                        hasAttribute("method", equalToIgnoringCase("GET"))));
        assertThat("header", header,
                hasUniqueSelector("#search-box form", hasChildren(keywordInputField(), searchButton())));
    }

    @SuppressWarnings("unchecked")
    private Matcher<Element> keywordInputField() {
        return anElement(hasTag("input"), hasId("keyword"), hasAttribute("type", "text"), hasAttribute("name", "keyword"));
    }

    @SuppressWarnings("unchecked")
    private Matcher<Element> searchButton() {
        return anElement(hasTag("button"), hasId("search"));
    }
}
