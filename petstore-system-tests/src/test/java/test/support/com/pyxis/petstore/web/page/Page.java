package test.support.com.pyxis.petstore.web.page;

import com.objogate.wl.web.AsyncWebDriver;
import com.pyxis.matchers.ExceptionImposter;
import test.support.com.pyxis.petstore.web.Routes;

import java.lang.reflect.Constructor;

public abstract class Page {

    protected final AsyncWebDriver browser;

    protected Page(AsyncWebDriver browser) {
        this.browser = browser;
    }

    public static HomePage home(AsyncWebDriver browser) {
        return new HomePage(browser);
    }

    public HomePage homePage() {
        return new HomePage(browser);
    }

    public CartPage cartPage() {
        return new CartPage(browser);
    }

    public ItemsPage itemsPage() {
        return new ItemsPage(browser);
    }

    public ProductsPage productsPage() {
        return new ProductsPage(browser);
    }

    public ReceiptPage receiptPage() {
        return new ReceiptPage(browser);
    }

    public PurchasePage purchasePage() {
        return new PurchasePage(browser);
    }

    public <T> T an(Class<T> pageClass) {
        try {
            Constructor<T> ctor = pageClass.getConstructor(AsyncWebDriver.class);
            return ctor.newInstance(browser);
        } catch (Exception e) {
            throw ExceptionImposter.imposterize(e);
        }
    }

    public void go() {
        browser.navigate().to(Routes.urlFor(getClass()));
    }

//    private void turnIntoProperPageSourceMatcher() {
//        if (webdriver.getTitle().equals("Apache Tomcat/5.5.20 - Error report")) {
//            Pattern pattern = Pattern.compile("<b>root cause</b>.*?<pre>(.*)</pre>", Pattern.DOTALL);
//            java.util.regex.Matcher matcher = pattern.matcher(webdriver.getPageSource());
//            boolean found = matcher.find();
//            String error = matcher.group(1);
//            failWith(error);
//        }
//    }
}
