package test.support.org.testinfected.petstore.web;

import org.testinfected.petstore.WebLayout;

import java.io.File;

public class WebRoot {

    public static final String WEB_ROOT = "web.root";
    public static final String RELATIVE_WEB_APP_PATH = "webapp/src/main/webapp";

    public static WebLayout locate() {
        File root = new File(System.getProperty(WEB_ROOT, RELATIVE_WEB_APP_PATH));
        return WebLayout.standard(root);
    }
}
