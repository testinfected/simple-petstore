package system.com.pyxis.petstore;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SearchPage {

    private final WebDriver webdriver;

    public SearchPage(WebDriver webdriver) {
        this.webdriver = webdriver;
        assertOnSearchPage();
    }

    private void assertOnSearchPage() {
        displaysTitle("PetStore - Search");
    }

    public void displaysTitle(final String title) {
        WebElement element = webdriver.findElement(By.tagName("title"));
        assertThat(element.getText(), is(title));
    }
}
