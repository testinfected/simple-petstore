package test.system.com.pyxis.petstore.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import test.support.com.pyxis.petstore.web.PageObject;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

public class ProductsPage extends PageObject {

    @FindBy(tagName = "title")
    private WebElement title;

    @FindBy(id = "match-count")
    private WebElement numberOfMatches;

    public ProductsPage(WebDriver webDriver) {
        super(webDriver);
    }

    public void displays(List<?> expectedProducts) {
        String text = numberOfMatches.getText();
        Integer numberOfProductsFound = Integer.parseInt(text);
        assertThat(numberOfProductsFound, is(expectedProducts.size()));
    }

    @Override
    public void assertLocation() {
        displaysTitle("PetStore - Products");
    }

    private void displaysTitle(final String expected) {
        assertThat(title.getText(), is(expected));
    }

}
