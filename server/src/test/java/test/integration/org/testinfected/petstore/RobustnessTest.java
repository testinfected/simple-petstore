package test.integration.org.testinfected.petstore;

import com.vtence.molecule.FailureReporter;
import com.vtence.molecule.testing.http.HttpRequest;
import com.vtence.molecule.testing.http.HttpResponse;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.testinfected.petstore.Environment;
import org.testinfected.petstore.PetStore;
import org.testinfected.petstore.Settings;
import org.testinfected.petstore.lib.Configuration;
import test.support.org.testinfected.petstore.web.WebRoot;

import java.io.IOException;
import java.sql.SQLException;

import static com.vtence.molecule.testing.http.HttpResponseAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class RobustnessTest {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery() {{
        setThreadingPolicy(new Synchroniser());
    }};

    FailureReporter failureReporter = context.mock(FailureReporter.class);
    PetStore petstore = new PetStore("127.0.0.1", 9999);

    HttpRequest request = new HttpRequest(9999);
    HttpResponse response;

    @Before public void
    configureServer() {
        petstore.reportErrorsTo(failureReporter);
    }

    @After
    public void stopServer() throws Exception {
        petstore.stop();
    }

    @Test
    public void respondsWith500WhenAnInternalServerOccurs() throws Exception {
        petstore.start(WebRoot.locate(), invalidDatabaseSettings());

        context.checking(new Expectations() {{
            oneOf(failureReporter).errorOccurred(with(any(SQLException.class)));
        }});

        response = request.get("/products");
        assertThat(response).hasStatusCode(500)
                            .hasBodyText(containsString("SQLException"))
                            .isNotChunked();
    }

    private Settings invalidDatabaseSettings() throws IOException {
        Configuration configuration = Environment.test();
        configuration.set("jdbc.username", "bad username");
        return new Settings(configuration);
    }
}