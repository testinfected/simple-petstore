package test.system.com.pyxis.petstore.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import test.system.com.pyxis.petstore.support.PageObject;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

public class SearchResultsPage extends PageObject {

    @FindBy(tagName = "title")
    private WebElement title;

    @FindBy(id = "match-count")
    private WebElement numberOfMatches;

    public SearchResultsPage(WebDriver webDriver) {
        super(webDriver);
    }

    public void displays(List<?> expectedItems) {
        String text = numberOfMatches.getText();
        Integer numberOfItemsFound = Integer.parseInt(text);
        assertThat(numberOfItemsFound, is(expectedItems.size()));
    }

    @Override
    public void assertLocation() {
        displaysTitle("PetStore - Search Results");
    }

    private void displaysTitle(final String expected) {
        assertThat(title.getText(), is(expected));
    }

}
