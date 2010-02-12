package system.com.pyxis.petstore.page;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import system.com.pyxis.petstore.support.PageObject;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SearchPage extends PageObject {

    @FindBy(tagName = "title")
    private WebElement title;

    public void assertOnRightPage() {
        displaysTitle("PetStore - Search");
    }

    public void displaysTitle(final String expected) {
        assertThat(title.getText(), is(expected));
    }

    public void search(String keyword) {
        
    }
}
