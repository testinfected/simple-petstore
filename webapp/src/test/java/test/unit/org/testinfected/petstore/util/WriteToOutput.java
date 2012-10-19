package test.unit.org.testinfected.petstore.util;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.jmock.api.Action;
import org.jmock.api.Invocation;

import java.io.Writer;

import static org.jmock.Expectations.any;

public class WriteToOutput implements Action {

    private Object content;

    public WriteToOutput(Object content) {
        this.content = content;
    }

    public Object invoke(Invocation invocation) throws Throwable {
        Writer writer = (Writer) invocation.getParameter(0);
        writer.write(content.toString());
        return null;
    }

    public void describeTo(Description description) {
        description.appendText("writes to output <").appendText(content.toString()).appendText(">");
    }

    public static Action writeToOutput(Object content) {
        return new WriteToOutput(content);
    }

    public static Matcher<Writer> anOutput() {
        return any(Writer.class);
    }
}
