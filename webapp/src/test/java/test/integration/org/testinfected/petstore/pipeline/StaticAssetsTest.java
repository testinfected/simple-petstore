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
import test.support.org.testinfected.petstore.web.WebRequestBuilder;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static test.support.org.testinfected.petstore.web.EmptyResponse.respondWithCode;
import static test.support.org.testinfected.petstore.web.HasStatusCode.hasStatusCode;
import static test.support.org.testinfected.petstore.web.WebRequestBuilder.aRequest;

public class StaticAssetsTest {

    int ASSET_SERVED = Status.FOUND.getCode();
    int NO_ASSET_SERVED = Status.NOT_FOUND.getCode();

    StaticAssets assets = new StaticAssets(respondWithCode(ASSET_SERVED), "/favicon.ico", "/static");
    Application application = new Application() {{
        use(assets);
        run(respondWithCode(NO_ASSET_SERVED));
    }};

    int SERVER_LISTENS_ON = 9999;
    Server server = new Server(SERVER_LISTENS_ON);

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
        send(aRequest().onPort(SERVER_LISTENS_ON).forPath("/favicon.ico"));
        assertThat("response", response, hasStatusCode(ASSET_SERVED));

        send(aRequest().onPort(SERVER_LISTENS_ON).forPath("/static/images/logo"));
        assertThat("response", response, hasStatusCode(ASSET_SERVED));
    }

    @Test public void
    forwardsToNextMiddlewareWhenPathIsNotMatched() throws Exception {
        send(aRequest().onPort(SERVER_LISTENS_ON).forPath("/home"));
        assertThat("response", response, hasStatusCode(NO_ASSET_SERVED));
    }

    private void send(WebRequestBuilder request) throws IOException {
        response = client.loadWebResponse(request.build());
    }

    WebClient client = new WebClient();
    WebResponse response;
}