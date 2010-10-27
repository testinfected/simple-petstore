package test.support.com.pyxis.petstore.web.page;

import com.objogate.wl.web.AsyncWebDriver;

public abstract class Page {

    protected final AsyncWebDriver browser;

    protected Page(AsyncWebDriver browser) {
        this.browser = browser;
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
