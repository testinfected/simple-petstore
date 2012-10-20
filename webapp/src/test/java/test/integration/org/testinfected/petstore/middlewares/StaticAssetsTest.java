package test.integration.org.testinfected.petstore.middlewares;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.simpleframework.http.Status;
import org.testinfected.petstore.Server;
import org.testinfected.petstore.middlewares.MiddlewareStack;
import org.testinfected.petstore.middlewares.StaticAssets;
import test.support.org.testinfected.petstore.web.HttpRequest;

import java.io.IOException;

import static test.support.org.testinfected.petstore.web.HttpRequest.aRequest;
import static test.support.org.testinfected.petstore.web.StaticResponse.respondWithCode;

public class StaticAssetsTest {

    int ASSET_SERVED = Status.FOUND.getCode();
    int NO_ASSET_SERVED = Status.NOT_FOUND.getCode();

    StaticAssets assets = new StaticAssets(respondWithCode(ASSET_SERVED), "/favicon.ico", "/static");

    Server server = new Server(9999);
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