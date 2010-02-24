package test.system.com.pyxis.petstore.page;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import test.support.com.pyxis.petstore.web.PageObject;

public class HomePage extends PageObject {

    @FindBy(id = "keyword")
    private WebElement queryField;

    @FindBy(id = "search")
    private WebElement searchButton;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    @Override
    public void assertLocation() {
		assertThat(queryField, is(notNullValue()));
		assertThat(searchButton, is(notNullValue()));
    }

	public ProductsPage searchFor(String keyword) {
        queryField.sendKeys(keyword);
        queryField.submit();
        return nowOn(ProductsPage.class);
	}

}
