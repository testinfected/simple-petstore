package test.support.com.pyxis.petstore.web.find;

import org.hamcrest.Factory;
import org.openqa.selenium.lift.find.HtmlTagFinder;

import static org.openqa.selenium.lift.Matchers.text;
import static com.pyxis.matchers.selenium.SeleniumMatchers.being;

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
