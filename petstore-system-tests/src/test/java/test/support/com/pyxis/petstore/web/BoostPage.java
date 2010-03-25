package test.support.com.pyxis.petstore.web;

import org.openqa.selenium.WebDriver;

import java.net.URL;

import static org.openqa.selenium.lift.Finders.link;

public class BoostPage extends PageObject {

    private static final URL BOOST_PAGE_URL = BoostPage.class.getResource("boost.html");

    private String windowHandle;

    public BoostPage(WebDriver webdriver) {
        super(webdriver);
    }

    public void load() {
        webdriver.navigate().to(BOOST_PAGE_URL);
        windowHandle = webdriver.getWindowHandle();
    }

    public void switchTo() {
        webdriver.switchTo().window(windowHandle);
    }

    public void openNewWindow() {
        clickOn(link("New test window"));
        webdriver.switchTo().window("test-window");
    }
}
