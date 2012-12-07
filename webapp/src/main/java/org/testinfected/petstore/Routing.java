package org.testinfected.petstore;

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
import org.testinfected.petstore.jdbc.ItemsDatabase;
import org.testinfected.petstore.jdbc.JDBCTransactor;
import org.testinfected.petstore.jdbc.OrderNumberDatabaseSequence;
import org.testinfected.petstore.jdbc.OrdersDatabase;
import org.testinfected.petstore.jdbc.ProductsDatabase;
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
import org.testinfected.support.Application;
import org.testinfected.support.RenderingEngine;
import org.testinfected.support.Request;
import org.testinfected.support.Response;
import org.testinfected.support.SimpleRequest;
import org.testinfected.support.SimpleResponse;
import org.testinfected.support.middlewares.Routes;
import org.testinfected.support.routing.Router;

import java.io.IOException;
import java.sql.Connection;

import static org.testinfected.petstore.util.SessionScope.sessionScopeOf;
import static org.testinfected.support.middlewares.ConnectionScope.ConnectionReference;

public class Routing implements Application {

    private final RenderingEngine renderer;
    private final Pages pages;

    public Routing(final RenderingEngine renderer) {
        this.renderer = renderer;
        this.pages = Pages.using(renderer);
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
            get("/products/:product/items").to(controller(new ListItems(itemInventory, pages.items())));
            post("/products/:product/items").to(controller(new CreateItem(requestHandler)));
            get("/cart").to(controller(new ShowCart(cashier)));
            post("/cart").to(controller(new CreateCartItem(cashier)));
            get("/orders/new").to(controller(new Checkout(cashier, pages.checkout())));
            get("/orders/:number").to(controller(new ShowOrder(orderBook)));
            post("/orders").to(controller(new PlaceOrder(cashier)));
            delete("/logout").to(controller(new Logout()));
            map("/").to(controller(new Home()));
        }});

        routes.handle(request, response);
    }

    private Application controller(final Controller controller) {
        return new Application() {
            public void handle(final Request req, final Response resp) throws Exception {
                controller.handle(
                        new SimpleRequest(new org.simpleframework.http.RequestWrapper(req.unwrap(org.simpleframework.http.Request.class)) {
                            public String getMethod() {
                                return req.method();
                            }

                            public String getParameter(String name) throws IOException {
                                return req.parameter(name);
                            }
                        }),
                        new SimpleResponse(resp.unwrap(org.simpleframework.http.Response.class), renderer, resp.charset()) {

                            public String contentType() {
                                return resp.contentType();
                            }
                        }
                );
            }
        };
    }
}
