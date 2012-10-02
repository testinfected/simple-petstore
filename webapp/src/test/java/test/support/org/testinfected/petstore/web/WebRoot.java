package test.support.org.testinfected.petstore.web;

import org.testinfected.petstore.PetStore;

import java.io.File;

public class WebRoot {

    public static final String WEB_ROOT = "web.root";
    public static final String RELATIVE_WEB_APP_PATH = "webapp/src/main/webapp";

    public static File locate() {
        return new File(System.getProperty(WEB_ROOT, RELATIVE_WEB_APP_PATH));
    }

    public static File layouts() {
        return new File(WebRoot.locate(), PetStore.LAYOUT_DIR);
    }

    public static File pages() {
        return new File(WebRoot.locate(), PetStore.PAGES_DIR);
    }
}
