package system.com.pyxis.petstore.page;

import org.openqa.selenium.WebDriver;
import system.com.pyxis.petstore.support.PageObject;

public class HomePage extends PageObject {

    public HomePage(WebDriver driver) {
        super(driver);
    }

    @Override
    public void assertOnRightPage() {
    }
}
