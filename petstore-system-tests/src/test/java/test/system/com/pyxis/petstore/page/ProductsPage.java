package test.system.com.pyxis.petstore.page;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import test.support.com.pyxis.petstore.web.PageObject;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ProductsPage extends PageObject {

    @FindBy(tagName = "title")
    private WebElement title;

    @FindBy(id = "match-count")
    private WebElement numberOfMatches;
    @FindBy(id = "products")
    private WebElement products;

    public ProductsPage(WebDriver webDriver) {
        super(webDriver);
    }

    public void displaysNoResult() {
        displaysNumberOfMatches(0);
    }

    public void displays(List<String> expectedProductNames) {
        displaysNumberOfMatches(expectedProductNames.size());
        displaysTableContaining(expectedProductNames);
    }

    private void displaysTableContaining(List<String> expectedProductNames) {
        List<WebElement> productRows = products.findElements(By.cssSelector("tr.product"));
        assertThat(productRows, hasSize(expectedProductNames.size()));

        List<WebElement> nameCells = products.findElements(By.cssSelector("td.name"));
        assertThat(expectedProductNames, contains(productNames(nameCells)));
    }

    private Matcher<Iterable<WebElement>> hasSize(int size) {
        return iterableWithSize(size);
    }

    private List<Matcher<? super String>> productNames(List<WebElement> nameCells) {
        List<Matcher<? super String>> nameMatchers = new ArrayList<Matcher<? super String>>();
        for (WebElement cell : nameCells) {
            nameMatchers.add(Matchers.is(cell.getText()));
        }
        return nameMatchers;
    }

    private void displaysNumberOfMatches(int matchCount) {
        String text = numberOfMatches.getText();
        Integer numberOfProductsFound = Integer.parseInt(text);
        assertThat(numberOfProductsFound, is(matchCount));
    }

    @Override
    public void assertLocation() {
        displaysTitle("PetStore - Products");
    }

    private void displaysTitle(final String expected) {
        assertThat(title.getText(), is(expected));
    }
}
