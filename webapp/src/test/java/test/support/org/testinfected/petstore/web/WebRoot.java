package test.support.org.testinfected.petstore.web;

import java.io.File;

public class WebRoot {

    public static final String WEB_ROOT = "web.root";
    public static final String RELATIVE_WEB_APP_PATH = "webapp/src/main/content";

    public static File locate() {
        return new File(System.getProperty(WEB_ROOT, RELATIVE_WEB_APP_PATH));
    }

    public static File layouts() {
        return new File(WebRoot.locate(), "views/layouts");
    }

    public static File pages() {
        return new File(WebRoot.locate(), "views/pages");
    }
}
