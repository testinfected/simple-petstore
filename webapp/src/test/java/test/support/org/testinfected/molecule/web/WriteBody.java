package test.support.org.testinfected.molecule.web;

import org.hamcrest.Description;
import org.jmock.api.Action;
import org.jmock.api.Invocation;
import org.testinfected.molecule.Response;

public class WriteBody implements Action {
    public static Action writeBody(String body) {
        return new WriteBody(body);
    }

    private String body;

    public WriteBody(String body) {
        this.body = body;
    }

    public Object invoke(Invocation invocation) throws Throwable {
        Response response = (Response) invocation.getParameter(1);
        response.body(body);
        return null;
    }

    public void describeTo(Description description) {
        description.appendText("writes body <").appendText(body).appendText(">");
    }
}
