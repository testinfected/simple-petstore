package org.testinfected.petstore;

import org.testinfected.petstore.controllers.Checkout;
import org.testinfected.petstore.controllers.CreateCartItem;
import org.testinfected.petstore.controllers.CreateItem;
import org.testinfected.petstore.controllers.CreateProduct;
import org.testinfected.petstore.controllers.ListItems;
import org.testinfected.petstore.controllers.ListProducts;
import org.testinfected.petstore.controllers.Logout;
import org.testinfected.petstore.controllers.PlaceOrder;
import org.testinfected.petstore.controllers.ShowCart;
import org.testinfected.petstore.controllers.ShowOrder;
import org.testinfected.petstore.controllers.StaticPage;
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
import org.testinfected.support.Request;
import org.testinfected.support.Response;
import org.testinfected.support.middlewares.Routes;
import org.testinfected.support.routing.Router;

import java.sql.Connection;

import static org.testinfected.petstore.util.SessionScope.sessionScopeOf;
import static org.testinfected.support.middlewares.ConnectionScope.ConnectionReference;

public class Routing implements Application {

    private final Pages pages;

    public Routing(final Pages pages) {
        this.pages = pages;
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
            get("/products").to(new ListProducts(productCatalog, attachmentStorage, pages.products()));
            post("/products").to(new CreateProduct(requestHandler));
            get("/products/:product/items").to(new ListItems(itemInventory, pages.items()));
            post("/products/:product/items").to(new CreateItem(requestHandler));
            get("/cart").to(new ShowCart(cashier, pages.cart()));
            post("/cart").to(new CreateCartItem(cashier));
            get("/orders/new").to(new Checkout(cashier, pages.checkout()));
            get("/orders/:number").to(new ShowOrder(orderBook, pages.order()));
            post("/orders").to(new PlaceOrder(cashier));
            delete("/logout").to(new Logout());
            map("/").to(new StaticPage(pages.home()));
        }});

        routes.handle(request, response);
    }
}
