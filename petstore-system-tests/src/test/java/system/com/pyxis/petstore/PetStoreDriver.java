package system.com.pyxis.petstore;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.lang.reflect.Constructor;

public class PetStoreDriver {

    private final WebDriver webdriver;

    public PetStoreDriver() {
        this.webdriver = new ChromeDriver();
    }

    public <T> T navigateTo(Class<T> pageClass) throws Exception {
        webdriver.get(Routes.urlFor(pageClass));
        Constructor<T> constructor = pageClass.getConstructor(WebDriver.class);
        return constructor.newInstance(webdriver);
    }

    public void dispose()
    {
        webdriver.close();
    }
}
