package test.system.com.pyxis.petstore.page;

import com.pyxis.petstore.domain.Product;
import org.openqa.selenium.WebDriver;
import test.support.com.pyxis.petstore.web.PageObject;

import static org.openqa.selenium.lift.Finders.cell;
import static org.openqa.selenium.lift.Finders.title;
import static org.openqa.selenium.lift.Matchers.exactly;
import static org.openqa.selenium.lift.Matchers.text;
import static test.support.com.pyxis.petstore.web.WebMatchers.being;
import static test.support.com.pyxis.petstore.web.WebMatchers.className;
import static test.support.com.pyxis.petstore.web.find.CssSelectorFinder.element;
import static test.support.com.pyxis.petstore.web.find.CssSelectorFinder.selector;

public class ProductsPage extends PageObject {

    public ProductsPage(WebDriver webDriver) {
        super(webDriver);
    }

    public void displaysNoResult() {
        assertPresenceOf(element("no-match"));
    }

    public void displays(Product... expectedProducts) {
        displaysNumberOfMatches(expectedProducts.length);
        displaysTableContaining(expectedProducts);
    }

    private void displaysTableContaining(Product... expectedProducts) {
        assertPresenceOf(exactly(expectedProducts.length), selector("tr.product"));

        for (Product expected : expectedProducts) {
            assertPresenceOf(cell().with(className("name")).with(text(being(expected.getName()))));
        }
    }

    private void displaysNumberOfMatches(int matchCount) {
        assertPresenceOf(element("match-count").with(text(being(matchCount))));
    }

    @Override
    public void assertLocation() {
        displaysTitle("PetStore - Products");
    }

    private void displaysTitle(final String expected) {
        assertPresenceOf(title(expected));
    }
}
