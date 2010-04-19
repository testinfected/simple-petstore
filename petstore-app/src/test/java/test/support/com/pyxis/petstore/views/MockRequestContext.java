package test.support.com.pyxis.petstore.views;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.EscapedErrors;
import org.springframework.web.servlet.support.RequestContext;

import java.util.HashMap;
import java.util.Map;

public class MockRequestContext extends RequestContext {

    private Map<String, BindingResult> bindings = new HashMap<String, BindingResult>();

    public void bind(BindingResult result) {
        bindings.put(result.getObjectName(), result);
    }

    @Override public Errors getErrors(String name, boolean htmlEscape) {
        if (!bindings.containsKey(name)) return noErrors(name);

        Errors errors = bindings.get(name);
        if (htmlEscape) errors = escape(errors);
        return errors;
    }

    private MockErrors noErrors(String name) {
        return new MockErrors(new HashMap(), name);
    }

    @Override public String getMessage(MessageSourceResolvable resolvable, boolean htmlEscape) throws NoSuchMessageException {
        if (errorCodesEmpty(resolvable)) throw new IllegalArgumentException("No error codes to get message from");
        return firstErrorCode(resolvable);
    }

    private String firstErrorCode(MessageSourceResolvable errors) {
        return errors.getCodes()[0];
    }

    private boolean errorCodesEmpty(MessageSourceResolvable errors) {
        return errors.getCodes().length == 0;
    }

    private EscapedErrors escape(Errors errors) {
        return new EscapedErrors(errors);
    }
}
