package system.com.pyxis.petstore.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import system.com.pyxis.petstore.support.PageObject;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SearchPage extends PageObject {

    @FindBy(tagName = "title")
    private WebElement title;

    @FindBy(id = "query")
    private WebElement queryField;

    @FindBy(id = "search")
    private WebElement searchButton;

    public SearchPage(WebDriver webDriver) {
        super(webDriver);
    }

    public void assertOnRightPage() {
        displaysTitle("PetStore - Search");
    }

    public SearchResultsPage searchFor(String keyword) {
        queryField.sendKeys(keyword);
        queryField.submit();
        return assertOn(SearchResultsPage.class);
    }

    private void displaysTitle(final String expected) {
        assertThat(title.getText(), is(expected));
    }
}
