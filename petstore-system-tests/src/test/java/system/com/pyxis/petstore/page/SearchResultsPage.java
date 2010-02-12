package system.com.pyxis.petstore.page;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import system.com.pyxis.petstore.support.PageObject;

public class SearchResultsPage extends PageObject {

    @FindBy(tagName = "title")
    private WebElement title;

    @FindBy(id = "numberOfItemsFound")
    private WebElement numberOfResultsElement;

    public void displays(List<Object> expectedItems) {
		String text = numberOfResultsElement.getText();
		Integer numberOfItemsFound = Integer.parseInt(text);
		assertThat(numberOfItemsFound, equalTo(expectedItems.size()));
	}

	@Override
	public void assertOnRightPage() {
        displaysTitle("PetStore - Search Results");		
	}

    public void displaysTitle(final String expected) {
        assertThat(title.getText(), is(expected));
    }

}
