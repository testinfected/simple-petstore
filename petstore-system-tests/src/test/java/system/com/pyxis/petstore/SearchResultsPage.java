package system.com.pyxis.petstore;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SearchResultsPage {

    private final WebDriver webdriver;

	public SearchResultsPage(WebDriver webdriver) {
		this.webdriver = webdriver;
	}

	public void displays(List<String> expectedItems) {
		WebElement numberOfResultsElement = this.webdriver.findElement(By.id("numberOfItemsFound"));
		Integer numberOfItemsFound = Integer.parseInt(numberOfResultsElement.getText());
		assertThat(numberOfItemsFound, equalTo(expectedItems.size()));
		assertAreDisplayed(expectedItems);
	}

	private void assertAreDisplayed(List<String> expectedItems) {
		List<WebElement> matchingItemElements = this.webdriver.findElements(By.id("match"));
		for (int i = 0; i < matchingItemElements.size(); i++) {
			assertThat(matchingItemElements.get(i).getText(), is(expectedItems.get(i)));
		}
	}

}
