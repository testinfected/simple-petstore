package test.support.com.pyxis.petstore.web;

import org.openqa.selenium.WebDriver;

import java.net.URL;

import static java.lang.String.valueOf;
import static org.openqa.selenium.lift.Finders.link;

public class ControlPage extends PageObject {

    private static final URL CONTROL_PAGE_URL = ControlPage.class.getResource("control.html");

    private String controlWindow;

    public ControlPage(WebDriver webdriver) {
        super(webdriver);
    }

    public void load() {
        webdriver.get(valueOf(CONTROL_PAGE_URL));
        controlWindow = webdriver.getWindowHandle();
    }

    public void newTestWindow() {
        switchTo(controlWindow);
        clickOn(link("New test window"));
        switchTo("test-window");
    }

    private void switchTo(final String windowHandle) {
        webdriver.switchTo().window(windowHandle);
    }
}
