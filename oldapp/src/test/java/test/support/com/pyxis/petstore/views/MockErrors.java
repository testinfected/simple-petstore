package test.support.com.pyxis.petstore.views;

import org.springframework.validation.MapBindingResult;

import java.util.HashMap;

public class MockErrors extends MapBindingResult {

	private static final long serialVersionUID = -4190022982496012574L;

	public MockErrors(final HashMap<?, ?> target, String objectName) {
        super(target, objectName);
    }

    public static MockErrors errorsOn(String objectName) {
        return new MockErrors(new HashMap<String, Object>(), objectName);
    }
}
