package test.support.org.testinfected.petstore.web;

public class WebRoot {

    public static String locate() {
        return System.getProperty("web.root", "webapp/src/main/webapp");
    }
}
