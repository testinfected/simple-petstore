package org.testinfected.petstore;

import com.pyxis.petstore.domain.order.Cart;
import com.pyxis.petstore.domain.order.Cashier;
import com.pyxis.petstore.domain.product.AttachmentStorage;
import com.pyxis.petstore.domain.product.ItemInventory;
import com.pyxis.petstore.domain.product.ProductCatalog;
import org.hibernate.jdbc.ConnectionManager;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.petstore.controllers.CreateCartItem;
import org.testinfected.petstore.controllers.CreateItem;
import org.testinfected.petstore.controllers.CreateProduct;
import org.testinfected.petstore.controllers.Home;
import org.testinfected.petstore.controllers.ListItems;
import org.testinfected.petstore.controllers.ListProducts;
import org.testinfected.petstore.controllers.Logout;
import org.testinfected.petstore.controllers.ShowCart;
import org.testinfected.petstore.jdbc.JDBCTransactor;
import org.testinfected.petstore.jdbc.ProductsDatabase;
import org.testinfected.petstore.procurement.ProcurementRequestHandler;
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
        final AttachmentStorage attachmentStorage = new FileSystemPhotoStore("/photos");

        final Connection connection = ConnectionManager.get(request);
        final Transactor transactor = new JDBCTransactor(connection);
        final ProductCatalog productCatalog = new ProductsDatabase(connection);
        final ItemInventory itemInventory = new ItemDatabase(connection);
        final ProcurementRequestHandler requestHandler = new PurchasingAgent(productCatalog, itemInventory, transactor);
        final Cart cart = new Cart();
        final Cashier cashier = new Cashier(null, null, itemInventory, cart);

        Routes routes = Routes.draw(new Router() {{
            get("/products").to(controller(new ListProducts(productCatalog, attachmentStorage)));
            post("/products").to(controller(new CreateProduct(requestHandler)));
            get("/products/:product/items").to(controller(new ListItems(itemInventory)));
            post("/products/:product/items").to(controller(new CreateItem(requestHandler)));
            get("/cart").to(controller(new ShowCart(cashier)));
            post("/cart").to(controller(new CreateCartItem(cashier)));
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
