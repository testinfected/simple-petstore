package test.support.com.pyxis.petstore.web.find;

import org.hamcrest.Factory;
import org.openqa.selenium.lift.find.HtmlTagFinder;

public class SelectFinder extends HtmlTagFinder {

    @Override protected String tagName() {
        return "select";
    }

    @Override protected String tagDescription() {
        return "select";
    }

    @Factory
    public static HtmlTagFinder selectionList() {
        return new SelectFinder();
    }
}