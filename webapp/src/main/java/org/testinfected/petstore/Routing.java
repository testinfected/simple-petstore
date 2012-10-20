package org.testinfected.petstore;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.petstore.controllers.CreateProduct;
import org.testinfected.petstore.controllers.Home;
import org.testinfected.petstore.controllers.ListProducts;
import org.testinfected.petstore.controllers.Logout;
import org.testinfected.petstore.jdbc.JDBCTransactor;
import org.testinfected.petstore.jdbc.ProductsDatabase;
import org.testinfected.petstore.middlewares.ConnectionManager;
import org.testinfected.petstore.procurement.PurchasingAgent;
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

            get("/products").to(controller(new ListProducts(new ProductsDatabase(connection), new FileSystemPhotoStore("/photos"))));
            post("/products").to(controller(new CreateProduct(new PurchasingAgent(new ProductsDatabase(connection), new JDBCTransactor(connection)))));
            delete("/logout").to(controller(new Logout()));
            map("/").to(controller(new Home()));
        }});
        routes.handle(request, response);
    }

    private Application controller(final Controller controller) {
        return new Application() {
            public void handle(Request request, Response response) throws Exception {
                controller.process(new SimpleRequest(request), new SimpleResponse(response, renderer, charset));
            }
        };
    }
}
