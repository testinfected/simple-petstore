package com.vtence.molecule.routing;

import com.vtence.molecule.Application;
import com.vtence.molecule.http.HttpMethod;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import com.vtence.molecule.middlewares.Router;
import com.vtence.molecule.support.MockResponse;
import org.junit.Test;

import static com.vtence.molecule.routing.DynamicRoutesTest.Echo.echo;
import static com.vtence.molecule.support.MockRequest.DELETE;
import static com.vtence.molecule.support.MockRequest.GET;
import static com.vtence.molecule.support.MockRequest.POST;
import static com.vtence.molecule.support.MockRequest.PUT;

public class DynamicRoutesTest {

    @Test public void
    opensRoutesMatchingSpecifiedPathsAndVerbs() throws Exception {
        Router router = Router.draw(new DynamicRoutes() {{
            map("/uri").via(HttpMethod.POST).to(echo("post to /uri"));
            map("/other/uri").via(HttpMethod.GET).to(echo("get to /other/uri"));
        }}).defaultsTo(echo("not matched"));

        dispatch(router, GET("/other/uri")).assertBody("get to /other/uri");
        dispatch(router, POST("/uri")).assertBody("post to /uri");
    }

    @Test public void
    providesConvenientShortcutsForDrawingRoutesUsingStandardVerbs() throws Exception {
        Router router = Router.draw(new DynamicRoutes() {{
            get("/").to(echo("get"));
            put("/").to(echo("put"));
            post("/").to(echo("post"));
            delete("/").to(echo("delete"));
        }}).defaultsTo(echo("not matched"));

        dispatch(router, GET("/")).assertBody("get");
        dispatch(router, POST("/")).assertBody("post");
        dispatch(router, PUT("/")).assertBody("put");
        dispatch(router, DELETE("/")).assertBody("delete");
    }

    @Test public void
    drawsRoutesInOrder() throws Exception {
        Router router = Router.draw(new DynamicRoutes() {{
            map("/").via(HttpMethod.GET).to(echo("get /"));
            map("/").to(echo("any /"));
        }}).defaultsTo(echo("not matched"));

        dispatch(router, GET("/"));
    }

    private MockResponse dispatch(Router router, Request request) throws Exception {
        MockResponse response = new MockResponse();
        router.handle(request, response);
        return response;
    }

    public static class Echo implements Application {

        private final String message;

        public static Application echo(String message) {
            return new Echo(message);
        }

        public Echo(String message) {
            this.message = message;
        }

        public void handle(Request request, Response response) throws Exception {
            response.body(message);
        }
    }
}
