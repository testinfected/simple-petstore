package test.integration.org.testinfected.petstore.pipeline;

import com.gargoylesoftware.htmlunit.WebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.simpleframework.http.Status;
import org.testinfected.petstore.Application;
import org.testinfected.petstore.Server;
import org.testinfected.petstore.pipeline.MiddlewareStack;
import org.testinfected.petstore.pipeline.StaticAssets;
import test.support.org.testinfected.petstore.web.WebRequestBuilder;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static test.support.org.testinfected.petstore.web.TextResponse.respondWithCode;
import static test.support.org.testinfected.petstore.web.HasStatusCode.hasStatusCode;
import static test.support.org.testinfected.petstore.web.WebRequestBuilder.aRequest;

public class StaticAssetsTest {

    int ASSET_SERVED = Status.FOUND.getCode();
    int NO_ASSET_SERVED = Status.NOT_FOUND.getCode();

    StaticAssets assets = new StaticAssets(respondWithCode(ASSET_SERVED), "/favicon.ico", "/static");
    Application application = new MiddlewareStack() {{
        use(assets);
        run(respondWithCode(NO_ASSET_SERVED));
    }};

    int SERVER_LISTENING_PORT = 9999;
    Server server = new Server(SERVER_LISTENING_PORT);
    WebRequestBuilder request = aRequest().onPort(SERVER_LISTENING_PORT);

    @Before public void
    startServer() throws IOException {
        server.run(application);
    }

    @After public void
    stopServer() throws Exception {
        server.shutdown();
    }

    @Test public void
    routesToFileServerWhenPathIsMatched() throws Exception {
        WebResponse response = request.but().forPath("/favicon.ico").send();
        assertThat("response", response, hasStatusCode(ASSET_SERVED));

        response = request.but().forPath("/static/images/logo").send();
        assertThat("response", response, hasStatusCode(ASSET_SERVED));
    }

    @Test public void
    forwardsToNextApplicationWhenPathIsNotMatched() throws Exception {
        WebResponse response = request.but().forPath("/home").send();
        assertThat("response", response, hasStatusCode(NO_ASSET_SERVED));
    }
}