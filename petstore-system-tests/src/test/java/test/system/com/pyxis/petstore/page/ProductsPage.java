package test.system.com.pyxis.petstore.page;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsEqual;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.lift.Finders;
import org.openqa.selenium.support.FindBy;
import test.support.com.pyxis.petstore.web.PageObject;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.valueOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.openqa.selenium.lift.Finders.*;
import static org.openqa.selenium.lift.Matchers.attribute;
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

    public void displays(List<String> expectedProductNames) {
        displaysNumberOfMatches(expectedProductNames.size());
        displaysTableContaining(expectedProductNames);
    }

    private void displaysTableContaining(List<String> expectedProductNames) {
        assertPresenceOf(exactly(expectedProductNames.size()), selector("tr.product"));

        for (String expected : expectedProductNames) {
            assertPresenceOf(cell().with(className("name")).with(text(being(expected))));
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
