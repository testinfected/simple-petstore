package org.testinfected.petstore;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.petstore.dispatch.EndPoint;
import org.testinfected.petstore.dispatch.SimpleRequest;
import org.testinfected.petstore.dispatch.SimpleResponse;
import org.testinfected.petstore.endpoints.Home;
import org.testinfected.petstore.endpoints.Logout;
import org.testinfected.petstore.endpoints.ShowProducts;
import org.testinfected.petstore.jdbc.ProductsDatabase;
import org.testinfected.petstore.pipeline.ConnectionManager;
import org.testinfected.petstore.routing.Router;
import org.testinfected.petstore.routing.Routes;
import org.testinfected.petstore.util.FileSystemPhotoStore;

import java.nio.charset.Charset;
import java.sql.Connection;

public class Routing implements Application {

    private final Renderer renderer;
    private final Charset charset;

    public Routing(final MustacheRendering renderer, final Charset charset) {
        this.renderer = renderer;
        this.charset = charset;
    }

    public void handle(Request request, Response response) throws Exception {
        final Connection connection = new ConnectionManager.ConnectionReference(request).get();
        Routes routes = drawRoutes(connection);
        routes.handle(request, response);
    }

    private Routes drawRoutes(final Connection connection) {
        Routes routes = new Routes();
        routes.draw(new Router() {{
            map("/products").to(endpoint(new ShowProducts(new ProductsDatabase(connection), new FileSystemPhotoStore("/photos"))));
            delete("/logout").to(endpoint(new Logout()));
            // todo match exactly
            map("/").to(endpoint(new Home()));
        }});
        return routes;
    }

    private Application endpoint(final EndPoint endPoint) {
        return new Application() {
            public void handle(Request request, Response response) throws Exception {
                endPoint.process(new SimpleRequest(request), new SimpleResponse(response, renderer, charset));
            }
        };
    }
}
