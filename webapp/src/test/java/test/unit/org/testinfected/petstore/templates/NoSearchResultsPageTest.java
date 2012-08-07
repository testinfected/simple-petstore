package test.unit.org.testinfected.petstore.templates;

import org.junit.Test;
import org.w3c.dom.Element;
import test.support.org.testinfected.petstore.web.OfflineRenderer;
import test.support.org.testinfected.petstore.web.WebRoot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.testinfected.hamcrest.dom.DomMatchers.hasText;
import static org.testinfected.hamcrest.dom.DomMatchers.hasUniqueSelector;
import static org.testinfected.petstore.util.Context.context;

public class NoSearchResultsPageTest {
    String NO_SEARCH_RESULTS_PAGE_TEMPLATE = "no-results";

    Element noProductPage;

    @Test public void
    indicatesThatNoMatchWasFound() {
        noProductPage = renderNoProductPage().using(context().with("keyword", "dog")).asDom();
        assertThat("products page", noProductPage, hasUniqueSelector("#no-match", hasText(containsString("dog"))));
    }

    private OfflineRenderer renderNoProductPage() {
        return OfflineRenderer.render(NO_SEARCH_RESULTS_PAGE_TEMPLATE).from(WebRoot.locatePages());
    }
}
