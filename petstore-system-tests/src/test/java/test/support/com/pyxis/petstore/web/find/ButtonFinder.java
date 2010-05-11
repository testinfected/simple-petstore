package test.support.com.pyxis.petstore.web.find;

import org.hamcrest.Factory;
import org.openqa.selenium.lift.find.HtmlTagFinder;

import static com.pyxis.matchers.core.CoreMatchers.being;
import static org.openqa.selenium.lift.Matchers.text;

public class ButtonFinder extends HtmlTagFinder {

    @Override protected String tagName() {
        return "button";
    }

    @Override protected String tagDescription() {
        return "button";
    }

    @Factory
    public static HtmlTagFinder button() {
        return new ButtonFinder();
    }

    @Factory
    public static HtmlTagFinder button(String buttonText) {
        return button().with(text(being(buttonText)));
    }
}
