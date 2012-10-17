package org.testinfected.petstore;

import org.testinfected.petstore.procurement.PurchasingAgent;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.petstore.controller.CreateProduct;
import org.testinfected.petstore.dispatch.EndPoint;
import org.testinfected.petstore.dispatch.SimpleRequest;
import org.testinfected.petstore.dispatch.SimpleResponse;
import org.testinfected.petstore.endpoints.Home;
import org.testinfected.petstore.endpoints.Logout;
import org.testinfected.petstore.endpoints.ShowProducts;
import org.testinfected.petstore.jdbc.JDBCTransactor;
import org.testinfected.petstore.jdbc.ProductsDatabase;
import org.testinfected.petstore.pipeline.ConnectionManager;
import org.testinfected.petstore.routing.Router;
import org.testinfected.petstore.routing.Routes;
import org.testinfected.petstore.util.FileSystemPhotoStore;

import java.nio.charset.Charset;
import java.sql.Connection;

public class Routing implements Application {

    private final RenderingEngine renderer;
    private final Charset charset;

    public Routing(final RenderingEngine renderer, final Charset charset) {
        this.renderer = renderer;
        this.charset = charset;
    }

    public void handle(final Request request, final Response response) throws Exception {
        Routes routes = Routes.draw(new Router() {{
            Connection connection = ConnectionManager.get(request);

            get("/products").to(endpoint(new ShowProducts(new ProductsDatabase(connection), new FileSystemPhotoStore("/photos"))));
            post("/products").to(new CreateProduct(new PurchasingAgent(new ProductsDatabase(connection), new JDBCTransactor(connection))));
            delete("/logout").to(endpoint(new Logout()));
            map("/").to(endpoint(new Home()));
        }});
        routes.handle(request, response);
    }

    private Application endpoint(final EndPoint endPoint) {
        return new Application() {
            public void handle(Request request, Response response) throws Exception {
                endPoint.process(new SimpleRequest(request), new SimpleResponse(response, renderer, charset));
            }
        };
    }
}
