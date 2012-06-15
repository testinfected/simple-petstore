package test.integration.org.testinfected.petstore.pipeline;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.simpleframework.http.Status;
import org.testinfected.petstore.Server;
import org.testinfected.petstore.pipeline.Application;
import org.testinfected.petstore.pipeline.StaticAssets;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static test.support.org.testinfected.petstore.web.Empty.empty;
import static test.support.org.testinfected.petstore.web.HasStatusCode.hasStatusCode;
import static test.support.org.testinfected.petstore.web.WebRequestBuilder.aRequest;

public class StaticAssetsTest {

    int PORT = 9999;
    Server server = new Server(PORT);

    StaticAssets assets = new StaticAssets(empty(Status.OK), "/favicon.ico", "/static");
    Application application = new Application() {{
        use(assets);
        // todo rename to emptyResponseWith
        // todo use FORWARDED = 30x for the code to clarify intention
        run(empty(Status.NOT_FOUND));
    }};

    WebClient client = new WebClient();
    WebResponse response;

    @Before public void
    startServer() throws IOException {
        client.setTimeout(5000);
        server.run(application);
    }

    @After public void
    stopServer() throws Exception {
        server.stop();
    }

    @Test public void
    routesToFileServerWhenPathIsMatched() throws Exception {
        response = client.loadWebResponse(aRequest().onPort(PORT).forPath("/favicon.ico").build());
        assertThat("response", response, hasStatusCode(200));

        response = client.loadWebResponse(aRequest().onPort(PORT).forPath("/static/images/logo").build());
        assertThat("response", response, hasStatusCode(200));
    }

    @Test public void
    forwardsToNextMiddlewareWhenPathIsNotMatched() throws Exception {
        response = client.loadWebResponse(aRequest().onPort(PORT).forPath("/home").build());
        assertThat("response", response, hasStatusCode(404));
    }
}