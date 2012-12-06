package org.testinfected.petstore;

import org.testinfected.petstore.controllers.*;
import org.testinfected.petstore.jdbc.*;
import org.testinfected.petstore.order.Cart;
import org.testinfected.petstore.order.Cashier;
import org.testinfected.petstore.order.OrderBook;
import org.testinfected.petstore.order.OrderNumberSequence;
import org.testinfected.petstore.procurement.ProcurementRequestHandler;
import org.testinfected.petstore.procurement.PurchasingAgent;
import org.testinfected.petstore.product.AttachmentStorage;
import org.testinfected.petstore.product.ItemInventory;
import org.testinfected.petstore.product.ProductCatalog;
import org.testinfected.petstore.util.FileSystemPhotoStore;
import org.testinfected.support.*;
import org.testinfected.support.middlewares.Routes;
import org.testinfected.support.routing.Router;

import java.nio.charset.Charset;
import java.sql.Connection;

import static org.testinfected.petstore.util.SessionScope.sessionScopeOf;
import static org.testinfected.support.middlewares.ConnectionScope.ConnectionReference;

public class Routing implements Application {

    private final RenderingEngine renderer;
    private final Charset charset;

    public Routing(final RenderingEngine renderer, final Charset charset) {
        this.renderer = renderer;
        this.charset = charset;
    }

    public void handle(org.simpleframework.http.Request request, org.simpleframework.http.Response response) throws Exception {
        handle(new SimpleRequest(request), new SimpleResponse(response, null, Charset.defaultCharset()));
    }

    public void handle(final Request request, final Response response) throws Exception {
        final AttachmentStorage attachmentStorage = new FileSystemPhotoStore("/photos");

        final Cart cart = sessionScopeOf(request.unwrap(org.simpleframework.http.Request.class)).cart();

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
            public void handle(org.simpleframework.http.Request request, org.simpleframework.http.Response response) throws Exception {
                handle(new SimpleRequest(request), new SimpleResponse(response, renderer, charset));
            }

            public void handle(Request request, Response response) throws Exception {
                controller.handle(request, response);
            }
        };
    }
}
