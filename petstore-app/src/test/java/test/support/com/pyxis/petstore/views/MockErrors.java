package test.support.com.pyxis.petstore.views;

import org.springframework.validation.MapBindingResult;

import java.util.HashMap;

public class MockErrors extends MapBindingResult {

    public MockErrors(final HashMap target, String objectName) {
        super(target, objectName);
    }

    public static MockErrors errorsOn(String objectName) {
        return new MockErrors(new HashMap(), objectName);
    }
}
