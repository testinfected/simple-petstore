package test.support.com.pyxis.petstore.web;

import org.openqa.selenium.WebDriver;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class WebDriverWindow implements InvocationHandler {

    private final WebDriver webdriver;
    private final String windowHandle;

    public static WebDriver newInstance(WebDriver webdriver) {
        return (WebDriver) java.lang.reflect.Proxy.newProxyInstance(
                webdriver.getClass().getClassLoader(),
                webdriver.getClass().getInterfaces(),
                new WebDriverWindow(webdriver));
    }

    private WebDriverWindow(WebDriver webdriver) {
        this.webdriver = webdriver;
        this.windowHandle = webdriver.getWindowHandle();
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (closeCalled(method) || quitClosed(method)) {
            closeWindow();
            return null;
        } else {
            return method.invoke(webdriver, args);
        }
    }

    private boolean quitClosed(Method method) {
        return method.getName().equals("quit");
    }

    private boolean closeCalled(Method method) {
        return method.getName().equals("close");
    }

    private void closeWindow() {
        webdriver.switchTo().window(windowHandle);
        webdriver.close();
    }
}