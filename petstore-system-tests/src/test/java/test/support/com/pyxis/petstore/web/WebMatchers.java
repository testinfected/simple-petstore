package test.support.com.pyxis.petstore.web;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsEqual;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.lift.match.AttributeMatcher;

public class WebMatchers {

    private WebMatchers() {}

    public static Matcher<WebElement> className(String className) {
        return AttributeMatcher.attribute("class", being(className));
    }

    public static Matcher<String> being(Object operand) {
        return new IsEqual<String>(String.valueOf(operand));
    }
}
