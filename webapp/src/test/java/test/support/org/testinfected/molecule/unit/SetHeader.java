package test.support.org.testinfected.molecule.unit;

import org.hamcrest.Description;
import org.jmock.api.Action;
import org.jmock.api.Invocation;
import org.testinfected.molecule.Response;

public class SetHeader implements Action {
    public static Action setHeader(String name, Object value) {
        return new SetHeader(name, value);
    }

    private final String name;
    private final Object value;

    public SetHeader(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public Object invoke(Invocation invocation) throws Throwable {
        Response response = (Response) invocation.getParameter(1);
        response.header(name, String.valueOf(value));
        return null;
    }

    public void describeTo(Description description) {
        description.appendText("sets header ").appendValue(name).appendText(" to ").appendValue(value);
    }
}
