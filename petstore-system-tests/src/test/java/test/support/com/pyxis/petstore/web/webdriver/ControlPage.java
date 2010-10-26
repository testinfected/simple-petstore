package test.support.com.pyxis.petstore.web.webdriver;

import org.openqa.selenium.WebDriver;

import java.net.URL;

import static java.lang.String.valueOf;
import static org.openqa.selenium.By.linkText;

public class ControlPage  {

    private static final URL CONTROL_PAGE_URL = ControlPage.class.getResource("control.html");

    private String controlWindow;
    private final WebDriver webdriver;

    public ControlPage(WebDriver webdriver) {
        this.webdriver = webdriver;
    }

    public void load() {
        webdriver.navigate().to(valueOf(CONTROL_PAGE_URL));
        controlWindow = webdriver.getWindowHandle();
    }

    public void newTestWindow() {
        switchTo(controlWindow);
        webdriver.findElement(linkText("New test window")).click();
        switchTo("test-window");
    }

    private void switchTo(final String windowHandle) {
        webdriver.switchTo().window(windowHandle);
    }
}
