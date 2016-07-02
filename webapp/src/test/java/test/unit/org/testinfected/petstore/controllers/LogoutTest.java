package test.unit.org.testinfected.petstore.controllers;

import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import com.vtence.molecule.session.Session;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.testinfected.petstore.controllers.Logout;

import static com.vtence.molecule.testing.ResponseAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class LogoutTest {

    Logout logout = new Logout();

    Session session = new Session();
    Request request = new Request();
    Response response = new Response();

    @Before public void
    bindSession() {
        session.bind(request);
    }

    @Test public void
    destroysSessionAndRedirectsToHomePageOnLogout() throws Exception {
        logout.handle(request, response);

        assertThat(response).isRedirectedTo("/")
                            .isDone();
        Assert.assertThat("session invalidated", session.invalid(), is(true));
    }
}
