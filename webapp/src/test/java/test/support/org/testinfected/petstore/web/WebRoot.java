package test.support.org.testinfected.petstore.web;

import org.testinfected.petstore.PetStore;

import java.io.File;

public class WebRoot {

    public static final String WEB_ROOT = "web.root";
    public static final String RELATIVE_WEB_APP_PATH = "webapp/src/main/webapp";

    public static File locatePages() {
        return locate(PetStore.TEMPLATE_DIRECTORY + "/" + PetStore.PAGES_DIRECTORY);
    }

    public static File locateLayout() {
        return locate(PetStore.TEMPLATE_DIRECTORY + "/" + PetStore.LAYOUT_DIRECTORY);
    }

    public static File locate(String relativePath) {
        return new File(locate(), relativePath);
    }

    public static File locate() {
        return new File(System.getProperty(WEB_ROOT, RELATIVE_WEB_APP_PATH));
    }
}
