package test.integration.org.testinfected.molecule.middlewares;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.simpleframework.http.Status;
import org.testinfected.molecule.Server;
import org.testinfected.molecule.simple.SimpleServer;
import org.testinfected.molecule.MiddlewareStack;
import org.testinfected.molecule.middlewares.StaticAssets;
import test.support.org.testinfected.molecule.web.HttpRequest;

import java.io.IOException;

import static test.support.org.testinfected.molecule.web.HttpRequest.aRequest;
import static test.support.org.testinfected.molecule.web.StaticResponse.respondWithCode;

// todo Consider rewriting as unit test now that we can mock requests and responses
public class StaticAssetsTest {

    int ASSET_SERVED = Status.FOUND.getCode();
    int NO_ASSET_SERVED = Status.NOT_FOUND.getCode();

    StaticAssets assets = new StaticAssets(respondWithCode(ASSET_SERVED), "/favicon.ico", "/static");

    Server server = new SimpleServer(9999);
    HttpRequest request = aRequest().to(server);

    @Before public void
    startServer() throws IOException {
        server.run(new MiddlewareStack() {{
            use(assets);
            run(respondWithCode(NO_ASSET_SERVED));
        }});
    }

    @After public void
    stopServer() throws Exception {
        server.shutdown();
    }

    @Test public void
    routesToFileServerWhenPathIsMatched() throws Exception {
        request.get("/favicon.ico").assertHasStatusCode(ASSET_SERVED);
        request.get("/static/images/logo").assertHasStatusCode(ASSET_SERVED);
    }

    @Test public void
    forwardsToNextApplicationWhenPathIsNotMatched() throws Exception {
        request.get("/home").assertHasStatusCode(NO_ASSET_SERVED);
    }
}