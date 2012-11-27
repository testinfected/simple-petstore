package org.testinfected.petstore;

import org.testinfected.petstore.order.Cart;
import org.testinfected.petstore.order.Cashier;
import org.testinfected.petstore.order.OrderBook;
import org.testinfected.petstore.order.OrderNumberSequence;
import org.testinfected.petstore.product.AttachmentStorage;
import org.testinfected.petstore.product.ItemInventory;
import org.testinfected.petstore.product.ProductCatalog;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.petstore.controllers.Checkout;
import org.testinfected.petstore.controllers.CreateCartItem;
import org.testinfected.petstore.controllers.CreateItem;
import org.testinfected.petstore.controllers.CreateProduct;
import org.testinfected.petstore.controllers.Home;
import org.testinfected.petstore.controllers.ListItems;
import org.testinfected.petstore.controllers.ListProducts;
import org.testinfected.petstore.controllers.Logout;
import org.testinfected.petstore.controllers.PlaceOrder;
import org.testinfected.petstore.controllers.ShowCart;
import org.testinfected.petstore.controllers.ShowOrder;
import org.testinfected.petstore.jdbc.*;
import org.testinfected.petstore.jdbc.ItemsDatabase;
import org.testinfected.petstore.procurement.ProcurementRequestHandler;
import org.testinfected.petstore.procurement.PurchasingAgent;
import org.testinfected.petstore.routing.Router;
import org.testinfected.petstore.routing.Routes;
import org.testinfected.petstore.util.FileSystemPhotoStore;

import java.nio.charset.Charset;
import java.sql.Connection;

import static org.testinfected.petstore.SessionScope.sessionScopeOf;
import static org.testinfected.petstore.middlewares.ConnectionScope.ConnectionReference;

public class Routing implements Application {

    private final RenderingEngine renderer;
    private final Charset charset;

    public Routing(final RenderingEngine renderer, final Charset charset) {
        this.renderer = renderer;
        this.charset = charset;
    }

    public void handle(final Request request, final Response response) throws Exception {
        final AttachmentStorage attachmentStorage = new FileSystemPhotoStore("/photos");

        final Cart cart = sessionScopeOf(request).cart();

        final Connection connection = new ConnectionReference(request).get();
        final Transactor transactor = new JDBCTransactor(connection);
        final ProductCatalog productCatalog = new ProductsDatabase(connection);
        final ItemInventory itemInventory = new ItemsDatabase(connection);
        final ProcurementRequestHandler requestHandler = new PurchasingAgent(productCatalog, itemInventory, transactor);
        final OrderNumberSequence orderNumberSequence = new OrderNumberDatabaseSequence(connection);
        final OrderBook orderBook = new OrdersDatabase(connection);
        final Cashier cashier = new Cashier(orderNumberSequence, orderBook, itemInventory, cart, transactor);

        Routes routes = Routes.draw(new Router() {{
            get("/products").to(controller(new ListProducts(productCatalog, attachmentStorage)));
            post("/products").to(controller(new CreateProduct(requestHandler)));
            get("/products/:product/items").to(controller(new ListItems(itemInventory)));
            post("/products/:product/items").to(controller(new CreateItem(requestHandler)));
            get("/cart").to(controller(new ShowCart(cashier)));
            post("/cart").to(controller(new CreateCartItem(cashier)));
            get("/orders/new").to(controller(new Checkout(cashier)));
            get("/orders/:number").to(controller(new ShowOrder(orderBook)));
            post("/orders").to(controller(new PlaceOrder(cashier)));
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
